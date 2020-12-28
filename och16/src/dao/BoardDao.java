package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BoardDao {
	private static BoardDao instance;
	private BoardDao() {}
	public static BoardDao getInstance() {
		if (instance == null) {	
			instance = new BoardDao();		
			}
		return instance;
	}
	private Connection getConnection() {
		Connection conn = null;
		try {
			Context ctx = new InitialContext();
			DataSource ds = (DataSource)
				ctx.lookup("java:comp/env/jdbc/OracleDB");
			conn = ds.getConnection();
		}catch(Exception e) { 
			System.out.println(e.getMessage());	
			}
		return conn;
	}
	
	public Board select(int num) throws SQLException {
		Connection conn = null;	
		Statement stmt= null; 
		ResultSet rs = null;
		String sql = "select * from board where num="+num;
		Board board = new Board();
		try {
			conn = getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {				
				board.setNum(rs.getInt("num"));
				board.setWriter(rs.getString("writer"));
				board.setSubject(rs.getString("subject"));
				board.setContent(rs.getString("content"));
				board.setEmail(rs.getString("email"));
				board.setReadcount(rs.getInt("readcount"));
				board.setIp(rs.getString("ip"));
				board.setReg_date(rs.getDate("reg_date"));
				board.setRef(rs.getInt("ref"));
				board.setRe_level(rs.getInt("re_level"));
				board.setRe_step(rs.getInt("re_step"));
				System.out.println("BoardDao select board.getContent()-->"+board.getContent());
		}
		} catch(Exception e) {	System.out.println(e.getMessage()); 
		} finally {
			if (rs !=null)    rs.close();
			if (stmt != null) stmt.close();
			if (conn !=null)  conn.close();
		}
		return board;
	}
	public int getTotalCnt() throws SQLException {
		Connection conn = null;	
		Statement stmt= null; 
		ResultSet rs = null;    
		int tot = 0;
		String sql = "select count(*) from board";
		try {
			conn = getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) tot = rs.getInt(1);
		} catch(Exception e) {	
			System.out.println(e.getMessage()); 
		} finally {
			if (rs !=null) rs.close();
			if (stmt != null) stmt.close();
			if (conn !=null) conn.close();
		}
		return tot;
	}
	
	public List<Board> list(int startRow, int endRow) throws SQLException {
		List<Board> list = new ArrayList<Board>();
		
		Connection conn = null;	
		PreparedStatement pstmt= null; 
		ResultSet rs = null;  
		String sql = "SELECT * from (select rownum rn ,a.* "
				+ "from (select * from board order by ref desc,re_step) a )"
				+ "WHERE rn between ? and ?";
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				do {
					Board board = new Board();
					 //rs.get 메소드는 sql입력했을때 나오는 순서대로 숫자 입력.
					board.setNum(rs.getInt(2));
					board.setWriter(rs.getString(3));
					board.setSubject(rs.getString(4));
					board.setContent(rs.getString(5));
					board.setEmail(rs.getString(6));
					board.setReadcount(rs.getInt(7));
					board.setPasswd(rs.getString(8));
					board.setRef(rs.getInt(9));
					board.setRe_step(rs.getInt(10));
					board.setRe_level(rs.getInt(11));
					board.setIp(rs.getString(12));
					board.setReg_date(rs.getDate(13));
					list.add(board);
	
			    }while(rs.next());
	         }

			
		} catch(Exception e) {	
			System.out.println(e.getMessage()); 
		} finally {
			if (rs !=null) rs.close();
			if (pstmt != null) pstmt.close();
			if (conn !=null) conn.close();
		}
		
		return list;
		
	}
	public void readCount(int num) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "update board set readcount=readcount+1 where num=?";
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}finally {
			if (pstmt != null) pstmt.close();
			if (conn !=null) conn.close();
		}
	}
	
	public int update(Board board) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result=0;
		String sql = "update board set subject=?, writer=?, email=?, passwd=?, content=? where num=?";
		System.out.println("BoardDao sql-->"+sql);
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, board.getSubject());
			pstmt.setString(2, board.getWriter());
			pstmt.setString(3, board.getEmail());
			pstmt.setString(4, board.getPasswd());
			pstmt.setString(5, board.getContent());
			pstmt.setInt(6, board.getNum());
			System.out.println("BoardDao board.getContent()-->"+board.getContent());
			
			result=pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if (pstmt != null) pstmt.close();
			if (conn !=null) conn.close();
		}
		
		return result;
	}
	
	//신규글작성
	public int insert(Board board) throws SQLException {
		int num = board.getNum();
		int result =0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql1 = "select nvl(max(num),0) from board"; //게시글 작성시 새로 부여되는 게시글 번호
		String sql = "insert into board values(?,?,?,?,?,?,?,?,?,?,?,sysdate)"; // DB에 값넣기
		String sql2 = "update board set re_step = re_step+1 where ref=? and re_step > ?"; //댓글 작성시 게시글과 댓글 사이를 파고 들어서 끼어들어가는 로직.. 
		
		try {
			conn = getConnection();
			
			//댓글일경우 추가작업, 
			if (num != 0) {
				pstmt = conn.prepareStatement(sql2);
				pstmt.setInt(1, board.getRef()); //댓글다는 게시물 번호 (그룹화)
				pstmt.setInt(2, board.getRe_step()); //ref 내의 순서
				pstmt.executeUpdate();
				pstmt.close();
				board.setRe_step(board.getRe_step()+1);
				board.setRe_level(board.getRe_level()+1);
			}
					

			pstmt = conn.prepareStatement(sql1);
			rs = pstmt.executeQuery();
			rs.next();
			//일단max사용
			int number = rs.getInt(1) +1;
			rs.close();
			pstmt.close();
			
			if(num == 0) board.setRef(number);
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, number); //게시글번호
			pstmt.setString(2, board.getWriter());
			pstmt.setString(3, board.getSubject());
			pstmt.setString(4, board.getContent());
			pstmt.setString(5, board.getEmail());
			pstmt.setInt(6, board.getReadcount());
			pstmt.setString(7, board.getPasswd());
			pstmt.setInt(8, board.getRef());
			pstmt.setInt(9, board.getRe_step());
			pstmt.setInt(10, board.getRe_level());
			pstmt.setString(11, board.getIp());
			result = pstmt.executeUpdate();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if (pstmt != null) pstmt.close();
			if (conn !=null) conn.close();
			if (rs !=null) rs.close();
			
		}return result;
	} 
	
	public int delete(int num, String passwd) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;
		ResultSet rs = null;
		String sql1 ="select passwd from board where num=?";
		String sql = "delete from board where num=?"; //삭제시 프라이머리키 조건 반드시 적어주기. 안그럼 테이블 다 날릴수도..
		
		try {
			String dbPasswd = "";
			conn = getConnection();
			pstmt = conn.prepareStatement(sql1);
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				dbPasswd = rs.getString(1);
				if (dbPasswd.equals(passwd)) {
					rs.close();
					pstmt.close();
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, num);
					result = pstmt.executeUpdate();				
				}else result = 0;
			}else result = -1;
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}finally {
			if(pstmt != null) pstmt.close();
			if(rs != null) rs.close();
			if(conn != null) conn.close();
		}
		
		
		return result;
	}
}
