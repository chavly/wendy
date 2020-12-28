package service;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Board;
import dao.BoardDao;

public class WriteFormAction implements CommandProcess {

	@Override //신규글만...
	public String requestPro(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
		int num = 0, ref =0, re_level=0, re_step=0; //초기값을 잡아줌.(댓글이 아니니 0 으로)
		
		String pageNum = request.getParameter("pageNum");
		System.out.println("WriteFormAction Start...");
		
		//댓글 작업
		if (request.getParameter("num") != null) {
			num = Integer.parseInt(request.getParameter("num"));
			BoardDao bd = BoardDao.getInstance();
			Board board = bd.select(num); 
			ref = board.getRef();
			re_level = board.getRe_level();
			re_step = board.getRe_step();
		}
			request.setAttribute("num", num);
			request.setAttribute("pageNum", pageNum);
			//댓글 작업할때 추가 해줘야 할 것
			request.setAttribute("ref", ref);
			request.setAttribute("re_level", re_level);
			request.setAttribute("re_step", re_step);
			
			} catch (SQLException e) {

				e.printStackTrace();
			}
		
		return "writeForm.jsp";
	}

}
