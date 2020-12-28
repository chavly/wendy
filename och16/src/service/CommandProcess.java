package service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface CommandProcess {
	//비지니스 업무를 이 인터페이스로 묶어 사용할 예정
	
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
	
}
