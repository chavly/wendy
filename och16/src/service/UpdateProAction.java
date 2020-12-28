package service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Board;
import dao.BoardDao;

public class UpdateProAction implements CommandProcess {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		int num = Integer.parseInt(request.getParameter("num"));
		
		try {
			String pageNum =request.getParameter("pageNum");
			Board board = new Board();
			board.setNum(Integer.parseInt(request.getParameter("num")));
			board.setWriter(request.getParameter("writer"));
			board.setEmail(request.getParameter("email"));
			board.setSubject(request.getParameter("subject"));
			board.setPasswd(request.getParameter("passwd"));
			board.setContent(request.getParameter("content"));
			
			System.out.println("UpdateProAction board.getContent()-->"+board.getContent());
			
			BoardDao bd = BoardDao.getInstance();
			int result = bd.update(board);
			System.out.println("UpdateProAction result-->"+result);
			System.out.println("UpdateProAction result-->"+result);
			request.setAttribute("result", result);
			request.setAttribute("num", num);
			request.setAttribute("pageNum", pageNum);
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return "updatePro.jsp";
	}

}
