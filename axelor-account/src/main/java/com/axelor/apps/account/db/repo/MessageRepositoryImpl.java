package com.axelor.apps.account.db.repo;

import com.axelor.db.Query;
import com.axelor.message.db.Message;
import com.axelor.message.db.repo.MessageRepository;

import java.util.List;


public class MessageRepositoryImpl extends MessageRepository {

    public List<Message> findByRelatedInvoiceId(Long invoiceId) {
        return Query.of(Message.class)
                .filter("self.sentByEmail = true")
                .filter("self.messageMultiRelatedList.relatedToSelect = :invoiceSelect")
                .filter("self.messageMultiRelatedList.relatedToSelectId = :invoiceId")
                .bind("invoiceSelect", "com.axelor.apps.account.db.Invoice")
                .bind("invoiceId", invoiceId)
                .fetch();
    }

}
