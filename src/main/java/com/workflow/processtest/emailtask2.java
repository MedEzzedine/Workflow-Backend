package com.workflow.processtest;

import java.io.Console;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class emailtask2 implements JavaDelegate{

	@Override
	public void execute(DelegateExecution execution) {

		System.out.println("traitement2.log");	
		System.out.println("iddemande"+execution.getVariable("iddemande"));	
	}
}

 