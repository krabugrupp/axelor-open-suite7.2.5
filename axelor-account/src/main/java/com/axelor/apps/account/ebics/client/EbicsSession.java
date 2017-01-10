/**
 * Axelor Business Solutions
 *
 * Copyright (C) 2017 Axelor (<http://axelor.com>).
 *
 * This program is free software: you can redistribute it and/or  modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axelor.apps.account.ebics.client;

/*
 * Copyright (c) 1990-2012 kopiLeft Development SARL, Bizerte, Tunisia
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * $Id$
 */


import java.io.IOException;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

import com.axelor.apps.account.db.EbicsUser;
import com.axelor.apps.account.ebics.service.EbicsCertificateService;
import com.axelor.exception.AxelorException;

/**
 * Communication hub for EBICS.
 *
 * @author Hachani
 *
 */
public class EbicsSession {
	
	  /**
	   * Constructs a new ebics session
	   * @param user the ebics user
	   * @param the ebics client configuration
	   */
	  public EbicsSession(EbicsUser user) {
	    this.user = user;
	    parameters = new HashMap<String, String>();
	  }

	  /**
	   * Returns the banks encryption key.
	   * The key will be fetched automatically form the bank if needed.
	   * @return the banks encryption key.
	   * @throws IOException Communication error during key retrieval.
	   * @throws EbicsException Server error message generated during key retrieval.
	   */
	  public RSAPublicKey getBankE002Key() throws AxelorException {
		
		  return  (RSAPublicKey) EbicsCertificateService.getCertificate(user.getEbicsPartner().getEbicsBank(), "encryption").getPublicKey();
	  }

	  /**
	   * Returns the banks authentication key.
	   * The key will be fetched automatically form the bank if needed.
	   * @return the banks authentication key.
	   * @throws IOException Communication error during key retrieval.
	   * @throws EbicsException Server error message generated during key retrieval.
	   */
	  public RSAPublicKey getBankX002Key() throws AxelorException {
		  return  (RSAPublicKey) EbicsCertificateService.getCertificate(user.getEbicsPartner().getEbicsBank(), "authentication").getPublicKey();
	  }

	  /**
	   * Returns the bank id.
	   * @return the bank id.
	   * @throws EbicsException
	   */
	  public String getBankID() throws AxelorException {
	    return user.getEbicsPartner().getEbicsBank().getHostId();
	  }

	  /**
	   * Return the session user.
	   * @return the session user.
	   */
	  public EbicsUser getUser() {
	    return user;
	  }

	  /**
	   * Sets the optional product identification that will be sent to the bank during each request.
	   * @param product Product description
	   */
	  public void setProduct(EbicsProduct product) {
	    this.product = product;
	  }

	  /**
	   * @return the product
	   */
	  public EbicsProduct getProduct() {
	    return product;
	  }

	  /**
	   * Adds a session parameter to use it in the transfer process.
	   * @param key the parameter key
	   * @param value the parameter value
	   */
	  public void addSessionParam(String key, String value) {
	    parameters.put(key, value);
	  }

	  /**
	   * Retrieves a session parameter using its key.
	   * @param key the parameter key
	   * @return the session parameter
	   */
	  public String getSessionParam(String key) {
	    if (key == null) {
	      return null;
	    }

	    return parameters.get(key);
	  }

	  // --------------------------------------------------------------------
	  // DATA MEMBERS
	  // --------------------------------------------------------------------

	  private EbicsUser				user;
	  private EbicsProduct				product;
	  private Map<String, String>			parameters;
	}
