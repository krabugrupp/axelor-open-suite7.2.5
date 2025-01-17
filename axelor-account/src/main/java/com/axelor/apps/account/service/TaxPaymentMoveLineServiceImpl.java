/*
 * Axelor Business Solutions
 *
 * Copyright (C) 2005-2024 Axelor (<http://axelor.com>).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.axelor.apps.account.service;

import com.axelor.apps.account.db.TaxPaymentMoveLine;
import com.axelor.apps.base.AxelorException;
import com.axelor.apps.base.service.app.AppBaseService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.apache.commons.collections.CollectionUtils;

public class TaxPaymentMoveLineServiceImpl implements TaxPaymentMoveLineService {

  @Override
  public TaxPaymentMoveLine computeTaxAmount(TaxPaymentMoveLine taxPaymentMoveLine)
      throws AxelorException {
    BigDecimal taxRate = taxPaymentMoveLine.getTaxRate().divide(new BigDecimal(100));
    BigDecimal base = taxPaymentMoveLine.getDetailPaymentAmount();
    taxPaymentMoveLine.setTaxAmount(
        base.multiply(taxRate)
            .setScale(AppBaseService.DEFAULT_NB_DECIMAL_DIGITS, RoundingMode.HALF_UP));

    if (isReverseTax(taxPaymentMoveLine)) {
      taxPaymentMoveLine.setTaxAmount(taxPaymentMoveLine.getTaxAmount().negate());
    }
    return taxPaymentMoveLine;
  }

  protected boolean isReverseTax(TaxPaymentMoveLine taxPaymentMoveLine) {
    return taxPaymentMoveLine.getFiscalPosition() != null
        && taxPaymentMoveLine.getFiscalPosition().getTaxEquivList().stream()
            .anyMatch(
                taxEquiv ->
                    CollectionUtils.isNotEmpty(taxEquiv.getReverseChargeTaxSet())
                        && taxEquiv
                            .getReverseChargeTaxSet()
                            .contains(taxPaymentMoveLine.getOriginTaxLine().getTax()));
  }

  @Override
  public TaxPaymentMoveLine getReverseTaxPaymentMoveLine(TaxPaymentMoveLine taxPaymentMoveLine)
      throws AxelorException {
    TaxPaymentMoveLine reversetaxPaymentMoveLine =
        new TaxPaymentMoveLine(
            taxPaymentMoveLine.getMoveLine(),
            taxPaymentMoveLine.getOriginTaxLine(),
            taxPaymentMoveLine.getReconcile(),
            taxPaymentMoveLine.getTaxRate(),
            taxPaymentMoveLine.getDetailPaymentAmount().negate(),
            taxPaymentMoveLine.getDate());
    reversetaxPaymentMoveLine = this.computeTaxAmount(reversetaxPaymentMoveLine);
    reversetaxPaymentMoveLine.setIsAlreadyReverse(true);
    reversetaxPaymentMoveLine.setVatSystemSelect(taxPaymentMoveLine.getVatSystemSelect());
    taxPaymentMoveLine.setIsAlreadyReverse(true);
    return reversetaxPaymentMoveLine;
  }
}
