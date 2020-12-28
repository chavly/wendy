package service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Board;
import dao.BoardDao;

public class UpdateFormAction implements CommandProcess {

	@Override //commandprocess 상속받아서 요부분이 추상메소드!
	public String requestPro(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			//나중에 수정할거임... 일단 맛보기...
		
			int num = Integer.parseInt(request.getParameter("num"));
			String pageNum =request.getParameter("pageNum");
			
			try {
			BoardDao bd = BoardDao.getInstance();
			Board board = bd.select(num); // 오라클 board테이블에 있는 1번 데이터를 전부 가져오고 싶은것임.!
			System.out.println("UpdateFormAction board.getContent()->"+board.getContent());
			request.setAttribute("pageNum", pageNum); //저~장!
			request.setAttribute("board", board);
			} catch (Exception e) {
				System.out.println("UpdateFormAction requestPro -->"+e.getMessage());
			}
		return "updateForm.jsp";
	}
	

}
