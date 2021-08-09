package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.QnaDTO;

public class QnaDAO extends JdbcDAO {
	public static QnaDAO _dao;

	public QnaDAO() {
		// TODO Auto-generated constructor stub
	}

	static {
		_dao = new QnaDAO();
	}

	public static QnaDAO getDAO() {
		return _dao;
	}

	// ��ü Q&A ��� - ��ü ��ȸ
	public int selectQna_Count(String search, String keyword) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		try {
			con = getConnection();

			if (keyword.equals("")) {// ���˻� ����� ������� ��������� ��, = ��ü�˻��� �Ѵٴ� ��
				String sql = "select count(*) from qna";
				pstmt = con.prepareStatement(sql);
			} else { // ���˻� ����� ����� ��� - where
				String sql = "select count(*) from qna where " + search + " like '%'||?||'%' and q_state!=9";
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, keyword);
			}

			rs = pstmt.executeQuery();

			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			System.out.println("[����] selectQna_Count() �޼ҵ��� SQL ���� = " + e.getMessage());
		} finally {
			close(con, pstmt, rs);
		}
		return count;
	}

	// ���� �� ~ ������ ������ �Խñ� ��� ��� [1���� ~ 10������]
	public List<QnaDTO> selectQna_List(int startRow, int endRow, String search, String keyword) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<QnaDTO> qnaList = new ArrayList<QnaDTO>();
		try {
			con = getConnection();

			if (keyword.equals("")) {
				String sql = "select * from (select rownum rn,temp.* from "
						+ "(select * from qna order by ref desc,re_step) temp) " + "where rn between ? and ?";
				pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, startRow);
				pstmt.setInt(2, endRow);
			} else {
				// �˻���� �߰� = where
				String sql = "select * from (select rownum rn,temp.* from " + "(select * from qna where " + search
						+ " like '%'||?||'%' and q_state!=9 "
						+ "order by ref desc,re_step) temp) where rn between ? and ?";
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, keyword);
				pstmt.setInt(2, startRow);
				pstmt.setInt(3, endRow);
			}

			rs = pstmt.executeQuery();

			while (rs.next()) {
				QnaDTO qna = new QnaDTO();
				qna.setQ_no(rs.getInt("q_no"));
				qna.setId(rs.getString("id"));
				qna.setQ_writer(rs.getString("q_writer"));
				qna.setQ_title(rs.getString("q_title"));
				qna.setQ_date(rs.getString("q_date"));
				qna.setReadcount(rs.getInt("readcount"));
				qna.setRef(rs.getInt("ref"));
				qna.setRe_Step(rs.getInt("re_step"));
				qna.setRe_Level(rs.getInt("re_level"));
				qna.setQ_content(rs.getString("q_content"));
				qna.setQ_state(rs.getInt("q_state"));
				qnaList.add(qna);
			}
		} catch (SQLException e) {
			System.out.println("[����]selectQna_List() �޼ҵ��� SQL ���� = " + e.getMessage());
		} finally {
			close(con, pstmt, rs);
		}
		return qnaList;
	}

	// ���̵� �������� �˻��� Q&A ��ü ��ȸ
	public List<QnaDTO> selectQna_Id(String id) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<QnaDTO> qnaList = new ArrayList<QnaDTO>();
		try {
			con = getConnection();
			// String sql = "select q_no,q_title,q_content,q_date,q_state from qna where
			// id=? order by q_date desc";
			String sql = "select * from qna where id=? order by q_date desc";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				QnaDTO qna = new QnaDTO();
				qna.setQ_no(rs.getInt("q_no"));
				qna.setQ_writer(rs.getString("q_writer"));
				qna.setQ_title(rs.getString("q_title"));
				qna.setQ_date(rs.getString("q_date"));
				qna.setQ_content(rs.getString("q_content"));
				qna.setQ_state(rs.getInt("q_state"));
				qna.setId(rs.getString("id"));
				qnaList.add(qna);

			}
		} catch (Exception e) {
			System.out.println("[����]selectQNA_Id() �޼ҵ��� SQL ���� = " + e.getMessage());
		} finally {
			close(con, pstmt, rs);
		}
		return qnaList;
	}

	// �۹�ȣ ���� �˻� - ������
	public QnaDTO selectQna_No(int no) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		QnaDTO qna = null;
		try {
			con = getConnection();

			String sql = "select * from qna where q_no=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, no);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				qna = new QnaDTO();
				qna.setQ_no(rs.getInt("q_no"));
				qna.setId(rs.getString("id"));
				qna.setQ_writer(rs.getString("q_writer"));
				qna.setQ_title(rs.getString("q_title"));
				qna.setQ_date(rs.getString("q_date"));
				qna.setReadcount(rs.getInt("readcount"));
				qna.setRef(rs.getInt("ref"));
				qna.setRe_Step(rs.getInt("re_step"));
				qna.setRe_Level(rs.getInt("re_level"));
				qna.setQ_content(rs.getString("q_content"));
				qna.setQ_state(rs.getInt("q_state"));
			
			}
		} catch (SQLException e) {
			System.out.println("[����]selectQna_No() �޼ҵ��� SQL ���� = " + e.getMessage());
		} finally {
			close(con, pstmt, rs);
		}
		return qna;
	}

	// �����ۼ��� ��������ü�� �̿��Ͽ� ������ �ڵ� ����
	public int selectNextQna() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int nextNum = 0;
		try {
			con = getConnection();

			String sql = "select qna_seq.nextval from dual";
			pstmt = con.prepareStatement(sql);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				nextNum = rs.getInt(1);
			}
		} catch (SQLException e) {
			System.out.println("[����]selectNextQna() �޼ҵ��� SQL ���� = " + e.getMessage());
		} finally {
			close(con, pstmt, rs);
		}
		return nextNum;
	}

	// �̵�� �亯 ��ȸ
	public List<QnaDTO> selectQNA_State() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<QnaDTO> qnaList = new ArrayList<QnaDTO>();
		try {
			con = getConnection();
			String sql = "select  * from qna where q_state=0";
			pstmt = con.prepareStatement(sql);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				QnaDTO qna = new QnaDTO();
				qna.setQ_no(rs.getInt("q_no"));
				qna.setId(rs.getString("q_id"));
				qna.setQ_title(rs.getString("q_title"));
				qna.setQ_content(rs.getString("q_content"));
				qna.setQ_date(rs.getString("q_date"));
				qna.setQ_state(rs.getInt("q_state"));
				qnaList.add(qna);
			}
		} catch (Exception e) {
			System.out.println("[����]selectQNA_State() �޼ҵ��� SQL ���� = " + e.getMessage());
		} finally {
			close(con, pstmt, rs);
		}
		return qnaList;
	}

	// Q&A ���[����] - ����
	public int insertQna(QnaDTO qna) {
		Connection con = null;
		PreparedStatement pstmt = null;
		int rows = 0;
		try {
			con = getConnection();

			String sql = "insert into qna values(?,?,?,?,sysdate,0,?,?,?,?,?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, qna.getQ_no());
			pstmt.setString(2, qna.getId());
			pstmt.setString(3, qna.getQ_writer());
			pstmt.setString(4, qna.getQ_title());
			pstmt.setInt(5, qna.getRef());
			pstmt.setInt(6, qna.getRe_Step());
			pstmt.setInt(7, qna.getRe_Level());
			pstmt.setString(8, qna.getQ_content());
			pstmt.setInt(9, qna.getQ_state());

			rows = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("[����]insertQna() �޼ҵ��� SQL ���� = " + e.getMessage());
		} finally {
			close(con, pstmt);

		}
		return rows;
	}
	//��ȸ�� ����
	public int updateQnaCount(int no) {
		Connection con=null;
		PreparedStatement pstmt=null;
		int rows=0;
		try {
			con=getConnection();
			
			String sql="update qna set readcount=readcount+1 where q_no=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, no);
			
			rows=pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("[����]updateQnaCount() �޼ҵ��� SQL ���� = "+e.getMessage());
		} finally {
			close(con, pstmt);
		}
		return rows;
	}

	// ��� �ޱ� [UPDATE] - ������
	public int AddQna_Ans(int ref, int reStep) {
		Connection con = null;
		PreparedStatement pstmt = null;
		int rows = 0;
		try {
			con = getConnection();

			String sql = "update qna set re_step=re_step+1 where ref=? and re_step>?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, ref);
			pstmt.setInt(2, reStep);

			rows = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("[����]AddQna_Ans() �޼ҵ��� SQL ���� = " + e.getMessage());
		} finally {
			close(con, pstmt);
		}
		return rows;
	}

	// ������ ���� �� ����
	public int ModifyQna_Ans(QnaDTO qna) {
		Connection con = null;
		PreparedStatement pstmt = null;
		int rows = 0;
		try {
			con = getConnection();

			String sql = "update qna set q_title=?,q_content=?,q_state=? where q_no=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, qna.getQ_title());
			pstmt.setString(2, qna.getQ_content());
			pstmt.setInt(3, qna.getQ_state());
			pstmt.setInt(4, qna.getQ_no());

			rows = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("[����]ModifyQna_Ans() �޼ҵ��� SQL ���� = " + e.getMessage());
		} finally {
			close(con, pstmt);
		}
		return rows;
	}

	// ����
	public int deleteQna_Ans(int num) {
		Connection con = null;
		PreparedStatement pstmt = null;
		int rows = 0;
		try {
			con = getConnection();

			String sql = "update qna set q_state=9 where q_no=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, num);

			rows = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("[����]deleteQna_Ans() �޼ҵ��� SQL ���� = " + e.getMessage());
		} finally {
			close(con, pstmt);
		}
		return rows;
	}
      //������������
	   //Q&A��ü���
    	public List<QnaDTO> selectAllQna() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<QnaDTO> qnaList = new ArrayList<QnaDTO>();
		
		try {
			con = getConnection();
			String sql = "select * from qna order by q_no";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				QnaDTO qna = new QnaDTO();
				qna.setQ_no(rs.getInt("q_no"));
				qna.setId(rs.getString("id"));
				qna.setQ_writer(rs.getString("q_writer"));
				qna.setQ_date(rs.getString("q_date"));
				qna.setReadcount(rs.getInt("readcount"));
				qna.setRef(rs.getInt("ref"));
				qna.setRe_Step(rs.getInt("re_step"));
				qna.setRe_Level(rs.getInt("re_level"));
				qna.setQ_content(rs.getString("q_content"));
				qna.setQ_state(rs.getInt("q_state"));
			}
		} catch (SQLException e) {
			System.out.println("[����] selectAllQna() �޼ҵ忡�� ���� �߻�");
		} finally {
			close(con, pstmt, rs);
		}
			
			return qnaList;
		}
}

    /*
	//�޼ҵ� �߰�(08/07) - ����
	//ȸ�� ���̵� ���� ���޹޾� ȸ���� �ۼ��� �Խñ��� REF�� �˻��Ͽ� ��ȯ�ϴ� �޼ҵ�
	public List<QnaDTO> selectRef(String id) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<QnaDTO> qnaList = new ArrayList<QnaDTO>();
		try {
			con = getConnection();
			String sql = "select ref from qna where id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				QnaDTO qna = new QnaDTO();
				qna.setRef(rs.getInt("ref"));
			}
		} catch (SQLException e) {
			System.out.println("[����] selectRef() �޼ҵ忡�� ���� �߻�");
		} finally {
			close(con, pstmt, rs);
		}
		
		return qnaList;
		}
	}
    	*/