package com.service;

import java.util.*;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.twilio.sdk.*;
import com.twilio.sdk.resource.factory.*;
import com.twilio.sdk.resource.instance.*;

public class TwilioService {
	// Find your Account Sid and Token at twilio.com/user/account
	public static final String ACCOUNT_SID = "<your account sid goes here>";
	public static final String AUTH_TOKEN = "your auth token goes here";
	public static Logger twilioLogger = LoggerFactory.getLogger(TwilioService.class);
	
	public void postMessage(String taskName) throws TwilioRestException {
		TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);

		// Build the parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("To", "<destination number>"));
		params.add(new BasicNameValuePair("From", "<source number>"));
		params.add(new BasicNameValuePair("Body", taskName + " has been done!"));

		MessageFactory messageFactory = client.getAccount().getMessageFactory();
		Message message = messageFactory.create(params);
		twilioLogger.info("Message sid:"+message.getSid());
	}
}