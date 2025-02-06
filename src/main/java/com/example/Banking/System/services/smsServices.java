package com.example.Banking.System.services;

import com.example.Banking.System.Configuration.TwilioSmsConfig;
import com.example.Banking.System.Configuration.TwilioWhatsappConfig;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class smsServices {
    @Autowired
    TwilioSmsConfig twilioSmsConfig;  // Injects Twilio SMS configuration

    @Autowired
    TwilioWhatsappConfig twilioWhatsappConfig; // Injects Twilio WhatsApp configuration


    public void sendSms(String smsNumber , String smsMessage){
        // Initialize Twilio with credentials
        Twilio.init(twilioSmsConfig.getAccountSid(),twilioSmsConfig.getAuthId());

        // Create and send the SMS message
        Message.creator(
                new PhoneNumber(smsNumber), // Recipient's phone number
                new PhoneNumber(twilioSmsConfig.getTrialNumber()), // Twilio's sender number
                smsMessage // Message content
        ).create();
    }

    public void whmessge(String smsNumber , String smsMessage){
        // Initialize Twilio with credentials
        Twilio.init(twilioSmsConfig.getAccountSid(),twilioSmsConfig.getAuthId());

        // Create and send the SMS message
        Message.creator(
                new PhoneNumber("whatsapp:"+smsNumber), // Recipient's phone number
                new PhoneNumber(twilioWhatsappConfig.getTrialNumber()), // Twilio's sender number
                smsMessage // Message conte
        ).create();
    }
}
