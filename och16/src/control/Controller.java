package control;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.CommandProcess;

// @WebServlet("/Controller")
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
			//command.properties.java 담을거임.
	//key : updateForm, value : service.UpdateFormAction
	private Map<String, Object> commandMap =  new HashMap<String, Object>(); 
     public Controller() {
        super();
        // TODO Auto-generated constructor stub
    }

 	// 무조건 한번 실행 응애~			web.xml config를 가져온다.
	public void init(ServletConfig config) throws ServletException {
	   	//web.xml에서 propertyConfig에 해당하는 (getInitParameter =>)init-param 의 값을 읽어옴
		String props = config.getInitParameter("config");  // <param-value>/WEB-INF/command.properties</param-value>
		//명령어와 처리클래스의 매핑정보를 저장할 Properties객체 생성
		Properties pr = new Properties(); // command.properties에서 키와 밸류로 구분!
		FileInputStream f = null; //파일을 읽어올수 있도록...작성함.
		try {		//metadata
			String configFilePath = config.getServletContext().getRealPath(props);
			f = new FileInputStream(configFilePath);
			// command.properties파일의 정보를  Properties객체에 저장
			pr.load(f);//속성이 메모리에 로드 된다. (프로퍼티에 파일이 올라간다. 키/밸류 분류지점)
			
		} catch (IOException  e) {
			 throw new ServletException(e);
		} finally {
		  if (f != null) try { f.close(); } catch(IOException ex) {}
		}
		
		//배열형태로 넣어주지만 배열은 아니다 ㅋㅋㅋ
		//Iterator객체는(배열처럼 쓸수 있도록 나열시킨 나열형) Enumeration객체를 확장시킨 개념의 객체
		//list.do  /updateForm.do
		Iterator keyIter = pr.keySet().iterator(); //ketset 키 가져오기
		
		//객체를 하나씩 꺼내서 그 객체명으로 Properties객체에 저장된 객체에 접근
		//hasNext() 있니?~ true or false
		while( keyIter.hasNext() ) {
	          String command = (String)keyIter.next();         // /list.do /updateForm.do
	          // className <= service.ListAction
	          String className = pr.getProperty(command); // getProperty 밸류가져오기 service.UpdateFormAction
	          
	          //ListAction la = new ListAction();
	          //updateFormAction ufa = new updateFormAction(); 를 하기와 같이 풀어 씀! 왜??
			//자동화로 인스턴스화 시켜서 만드는 것임..
	          try {
	               Class commandClass = Class.forName(className);//해당 문자열을 클래스로 만든다.
	               Object commandInstance = commandClass.newInstance();//해당클래스의 객체를 생성
	               commandMap.put(command, commandInstance); // Map객체인 commandMap에 객체 저장
	              //commandMap  command,   		commandInstance
	               // 			list.do   		service.ListAction
	               //	    	updateForm.do   service.UpdateFormAction
	          } catch (Exception e) {
	               throw new ServletException(e);
	          }		
		
	     }
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		   requestPro(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		   requestPro(request, response);
	}

	//시용자의 요청을 분석해서 해당 작업을 처리
	 // 브라우저(집에있는지예가) -> 컨트롤러(Servlet) -> Model(Dao) -> DTO -> 컨트롤러 -> View(화면을보여줘야해)
	private void requestPro(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String view = null;
	    CommandProcess com=null;
	    String command = request.getRequestURI(); // och16/list.do
	    try {	
				System.out.println("requestPro command 1=>"+ command);  // /och16/list.do
			//	System.out.println(request.getContextPath()); // /och16
			//	System.out.println(command.indexOf(request.getContextPath()));  // 0
	       //  if (command.indexOf(request.getContextPath()) == 0) {
	              command = command.substring(request.getContextPath().length());
	        //  }
	              
	              // listAction com = newListAction()
	          com = (CommandProcess)commandMap.get(command);  // /list.do
			  System.out.println("requestPro  command 2=>"+ command);  // /och16/com
			  System.out.println("requestPro com=> "+ com);          // /och16/com
	          
			  //service.ListAction.requestPro(request,response)
			  view = com.requestPro(request, response);				//updateForm.jsp
			  System.out.println("requestPro view=> "+ view);        // /och16/com
	    } catch(Throwable e) { 
	    	throw new ServletException(e); 
	    } 
	    RequestDispatcher dispatcher =   request.getRequestDispatcher(view);
        dispatcher.forward(request, response);
		
	}
	
}
