package com.workflow.processtest;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class emailtask1  implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) {
		
		System.out.println("traitement1.log");
		System.out.println("iddemande"+execution.getVariable("iddemande"));
	}

}
