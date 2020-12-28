package service;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Board;
import dao.BoardDao;

public class ListAction implements CommandProcess {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			BoardDao bd = BoardDao.getInstance();
		try { //startRow와 endRow가 계속 바뀌어가며 나온다.. 페이지 번호..
			String pageNum = request.getParameter("pageNum");
			if (pageNum==null || pageNum.equals("")) { pageNum ="1";}
			int currentPage = Integer.parseInt(pageNum);	//currentPage 2
			int pageSize = 10; //게시물 하나 단위!! 
			int blockSize =10; // 하단에 넘길 수 있는 최대 페이지 수 (다음 누르기전)
			int startRow = (currentPage - 1) * pageSize +1; //화면에서 보여지는 첫번째 행번호.. 초기값 1, currentPage 2 -->11
			int endRow = startRow + pageSize - 1; 			//화면에서 보여지는 마지막 행번호.. 초기값 10, currentPage 2 -->20
			int totCnt = bd.getTotalCnt();  //전체 게시글의 수 
			int startNum = totCnt - startRow + 1; //38 화면에서 보여지는 첫번째 행의 역순번호..
			List<Board> list = bd.list(startRow,endRow); //10개씩 증가
			int pageCnt = (int)Math.ceil((double)totCnt/pageSize); //게시판에 넘겨서 보여줄 수 있는 전체 페이지의 수
			int startPage = (int)(currentPage-1)/blockSize*blockSize+1; //이전 누르기 전의 가장 처음 페이지 번호.. 초기값 1, currentPage 2 --> 2 
			int endPage = startPage + blockSize -1;						//다음 누르기 전의 가장 마지막 페이지 번호.. 초기값 1, currentPage 2 --> 11
			if(endPage > pageCnt) endPage = pageCnt;
			
			request.setAttribute("totCnt", totCnt); 
			request.setAttribute("pageNum", pageNum); 
			request.setAttribute("currentPage", currentPage); 
			request.setAttribute("startNum", startNum);
			request.setAttribute("list", list); 
			request.setAttribute("blockSize", blockSize);
			request.setAttribute("pageCnt", pageCnt);
			request.setAttribute("startPage", startPage);
			request.setAttribute("endPage", endPage);
			
			System.out.println("----------------------------------------------"); //och16 list.do
			System.out.println("startNum -->" + startNum); //och16 list.do
			System.out.println("totCnt -->" + totCnt); //och16 list.do
			System.out.println("currentPage -->" + currentPage); //och16 list.do
			System.out.println("blockSize -->" + blockSize); //och16 list.do
			System.out.println("pageCnt -->" + pageCnt); //och16 list.do
			System.out.println("startPage -->" + startPage); //och16 list.do
			System.out.println("endPage -->" + endPage); //och16 list.do
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return "list.jsp";
	}

}
