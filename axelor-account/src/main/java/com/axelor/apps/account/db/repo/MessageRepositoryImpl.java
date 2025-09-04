package com.axelor.apps.account.db.repo;

import com.axelor.db.Query;
import com.axelor.message.db.Message;
import com.axelor.message.db.repo.MessageRepository;


public class MessageRepositoryImpl extends MessageRepository {

    public Message findByRelatedInvoiceId(Long invoiceId) {
        return Query.of(Message.class)
                .filter("self.relatedTo1Select = com.axelor.apps.account.db.Invoice")
                .filter("self.relatedTo1SelectId = :invoiceId")
                .bind("invoiceId", invoiceId)
                .fetchOne();
    }

}
