package com.service;

import java.util.*;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.twilio.sdk.*;
import com.twilio.sdk.resource.factory.*;
import com.twilio.sdk.resource.instance.*;

public class TwilioTest {
	// Find your Account Sid and Token at twilio.com/user/account
	public static final String ACCOUNT_SID = "<your account sid goes here>";
	public static final String AUTH_TOKEN = "your auth token goes here";

	public void postMessage(String taskName) throws TwilioRestException {
		TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);

		// Build the parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("To", "<destination number>"));
		params.add(new BasicNameValuePair("From", "<source number>"));
		params.add(new BasicNameValuePair("Body", taskName + " has been done!"));

		MessageFactory messageFactory = client.getAccount().getMessageFactory();
		Message message = messageFactory.create(params);
		System.out.println(message.getSid());
	}
}