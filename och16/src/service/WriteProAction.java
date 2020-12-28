package service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Board;
import dao.BoardDao;

public class WriteProAction implements CommandProcess {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
				
		try {
			String pageNum = request.getParameter("pageNum");
			Board board= new Board();
			board.setSubject(request.getParameter("subject"));
			board.setWriter(request.getParameter("writer"));
			board.setEmail(request.getParameter("email"));
			board.setContent(request.getParameter("content"));
			board.setPasswd(request.getParameter("passwd"));
			board.setIp(request.getRemoteAddr());
			
			//댓글용도
			board.setNum(Integer.parseInt(request.getParameter("num")));
			board.setRef(Integer.parseInt(request.getParameter("ref")));
			board.setRe_step(Integer.parseInt(request.getParameter("re_step")));
			board.setRe_level(Integer.parseInt(request.getParameter("re_level")));
			
			BoardDao bd = BoardDao.getInstance(); //DB
			int result = bd.insert(board);
			request.setAttribute("num", board.getNum());
			request.setAttribute("result", result);
			request.setAttribute("pageNum", pageNum);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return "writePro.jsp";
	}

}
