package com.fly.firefly.drawer;/*package me_tech.com.firez.drawer;

import com.silverlake.ifb.utils.App;
import com.silverlake.ifb.utils.soap.TaskInterface.OnTaskCompleted;
import com.silverlake.ifb.utils.soap.TaskInterface.OnTaskStarting;
import com.silverlake.ifb.utils.soap.VrsmSoapProperties;
import com.silverlake.ifb.utils.soap.VrsmSoapWebServices;
import com.silverlake.ifb.utils.soap.VrsmSoapWebServices.SoapTask;
import com.silverlake.ifb.utils.soap.bean.RequestBean;

public class InitialAppSoap
{

	public static void initialApp(OnTaskStarting onTaskStarting, OnTaskCompleted onTaskCompleted)
	{

		final String METHOD_NAME = "initialApp";
		final String SOAP_ACTION = "http://adapter.ifb.silverlake.com/GeneralWsServicesSoapBinding/initialApp";
		final String NAMESPACE = "http://adapter.ifb.silverlake.com/";

		RequestBean requestBean = new RequestBean();
		requestBean.setDeviceId("");
		requestBean.setHash("");
		requestBean.setLang(App.LANG);
		requestBean.setInitialApp(false);

		VrsmSoapProperties soapProperties = new VrsmSoapProperties();
		soapProperties.setMethodName(METHOD_NAME);
		soapProperties.setSoapAction(SOAP_ACTION);
		soapProperties.setNameSpace(NAMESPACE);
		soapProperties.setUrl(App.WS_URL);
		soapProperties.setRequestBeanObjectName("RequestBean");
		soapProperties.setRequestBeanObject(requestBean);

		SoapTask soapTask = new VrsmSoapWebServices.SoapTask(onTaskStarting);
		soapTask.setOnTaskCompleted(onTaskCompleted);
		soapTask.execute(soapProperties);
	}
}
*/