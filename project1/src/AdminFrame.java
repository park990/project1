//import ClassInfoDialog;
import pm.vo.AdminVO;
import pm.vo.ClassVO;
import pm.vo.MemberVO;
import pm.vo.StudentVO;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.io.Reader;
import java.util.List;

public class AdminFrame extends JFrame {


    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AdminFrame.class.getName());

    String[] c_name = {"학번", "이름", "전화번호", "이메일"}; //수강생 정보
    String[] a_name = {"강사번호", "이름", "전화번호", "이메일"}; //강사 정보
    String[] l_name = {"강의명", "강사명", "강의기간", "정원"}; //강의 정보

    String[][] tableData;

    SqlSessionFactory factory;

    List<StudentVO> stuList; //수강생
    List<AdminVO> adList; //관리자
    List<ClassVO> lecList; //강의


    JMenuBar bar;
    JMenu file_M;
    JMenuItem logOut_item, exit_item;

    int i;


    //생성자
    public AdminFrame() {
        initComponents(); //화면구성

        //메뉴작업
        bar = new JMenuBar();
        file_M = new JMenu("파일");

        logOut_item = new JMenuItem("로그아웃");
        exit_item = new JMenuItem("종료");

        //메뉴 아이템을 추가
        file_M.add(logOut_item);
        file_M.add(exit_item);

        bar.add(file_M);
        this.setJMenuBar(bar);

        this.setVisible(true); //창 띄우기

        //DB연결
        init();
        allStudentData();

        //이벤트 감지자 등록
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0); //프로그램 종료
            }
        });

        /*exit_item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        }*/

        //상단 탭을 눌렀을 때 이벤트 감지자 등록
        jTabbedPane1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int index = jTabbedPane1.getSelectedIndex();
                if (index == 0) { //학생탭 클릭 시
                    allStudentData();
                } else if (index == 1) { //강사 탭 클릭 시
                    allAdminData();
                } else if (index == 2) { //강의명 탭 클릭 시
                    allClassData();
                }
            }
        });

        //수강생 관리탭에서 수강생 테이블에 수강생을 더블클릭 했을 때 (stdTable)
        stdTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //테이블에서 더블 클릭 했을 때
                int cnt = e.getClickCount();
                if(cnt == 2){ //더블 클릭 시
                    //stdTable에서 선택된 행, index를 얻어내자
                    i = stdTable.getSelectedRow(); //List<StudentVO> 의 값을 얻어낼 수 있다.
                    StudentVO stVO = stuList.get(i);


                    // 학생의 학번을 통해 member테이블의 학번이랑 연결해서 강사의 강사번호가 null인것중 get(i)의 학번의 아이디를 갖고와서 화면에 표출하자


                    //System.out.println(stVO);
                    new StudentInfoDialog(AdminFrame.this, true, stVO);//**************************************

                }
            }
        });

        //강사 탭에서 강사 한명을 더블클릭 했을 때 (adTable)
        adTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                //테이블에서 더블 클릭 했을 때
                int cnt = e.getClickCount();
                if(cnt == 2){ //더블 클릭 시
                    //jTable1에서 선택된 행, index를 얻어내자
                    i = adTable.getSelectedRow(); //List<AdminVO> 의 값을 얻어낼 수 있다.
                    AdminVO adVO = adList.get(i);

                    new AdminInfoDialog(AdminFrame.this, true, adVO);
                }
            }
        });

        //강의 탭에서 강의를 더블클릭 했을 때 (lecTable)
        lecTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //테이블에서 더블 클릭 했을 때
                int cnt = e.getClickCount();
                if(cnt == 2){ //더블 클릭 시
                    //lecTable에서 선택된 행, index를 얻어내자
                    i = lecTable.getSelectedRow();
                    ClassVO cVO = lecList.get(i);

                    new ClassInfoDialog(AdminFrame.this, false, cVO);
                }
            }
        });

        //수강생 관리 탭 - 검색 버튼 클릭 시
        stdSearch_bt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //사용자가 입력한 값 추출
                String stdName = jTextField1.getText().trim();
                SqlSession ss = factory.openSession();

                if(stdName.length() > 0) {
                    stuList = ss.selectList("student.std_search", stdName);
                    studentViewTable(stuList);
                } else {
                    //입력을 하지 않은채로 검색을 누른 경우, 전체 학생 리스트를 출력한다.
                    //JOptionPane.showMessageDialog(AdminFrame.this, "수강생 이름을 입력 후 검색버튼을 눌러주세요.");
                    stuList = ss.selectList("student.all", stdName);
                    studentViewTable(stuList);
                }
                ss.close();
            }
        });


        //강사 관리 탭 - 검색 버튼 클릭 시 
        adSearch_bt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //사용자가 입력한 값 추출
                String adminName = jTextField2.getText().trim();
                SqlSession ss = factory.openSession();

                //강사 이름을 입력한 경우
                if(adminName.length() > 0) {
                    adList = ss.selectList("admin.admin_search", adminName);
                    adminViewTable(adList);
                } else {
                    //입력을 안한 경우
                    //JOptionPane.showMessageDialog(AdminFrame.this, "강사명을 입력 후 검색버튼을 눌러주세요.");
                    adList = ss.selectList("admin.all", adminName);
                    adminViewTable(adList);
                }
                ss.close();
            }
        });

        //강의관리 탭 - 검색 버튼 클릭 시
        lecSearch_bt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //사용자가 입력한 값 추출
                String lecName = jTextField3.getText().trim();

                if(lecName.length() > 0) {
                    SqlSession ss = factory.openSession();
                    lecList = ss.selectList("lec_t.lec_search", lecName);
                    ss.close();

                    lecViewTable(lecList);
                } else {
                    //입력을 안한 경우
                    JOptionPane.showMessageDialog(AdminFrame.this, "강의명을 입력 후 검색버튼을 눌러주세요.");
                }
            }
        });

        //강의관리 추가 버튼 클릭 시
        addLec_bt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddLecDialog(AdminFrame.this, true);
            }
        });


    } //생성자 끝



    //DB연결하는 함수
    private void init(){

        try{
            Reader r = Resources.getResourceAsReader("pm/config/conf.xml");
            factory = new SqlSessionFactoryBuilder().build(r);
            r.close();

            this.setTitle("관리자 페이지!");

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //학생 탭 클릭 시 전체 데이터 출력하는 함수
    private void allStudentData(){

        SqlSession ss = factory.openSession();
        stuList = ss.selectList("student.all");
        studentViewTable(stuList);
        ss.close();
    }

    //강사 탭 클릭 시 전체 데이터 출력하는 함수
    private void allAdminData(){

        SqlSession ss = factory.openSession();
        adList = ss.selectList("admin.all");
        adminViewTable(adList);
        ss.close();
    }

    //강의관리 탭 클릭 시 전체 데이터 출력하는 함수
    private void allClassData(){

        SqlSession ss = factory.openSession();
        lecList = ss.selectList("lec_t.all");
        lecViewTable(lecList);
        ss.close();
    }

    //인자로 받은 List구조를 2차원 배열로 변환한 후 JTable에 표현!
    private void studentViewTable(List<StudentVO> stuList){
        //stdTable에 데이터 세팅
        tableData = new String[stuList.size()][c_name.length];
        int i= 0;
        for(StudentVO stVO : stuList){
            tableData[i][0] = stVO.getStdno(); //수강생 학번
            tableData[i][1] = stVO.getStd_name(); //수강생 이름
            tableData[i][2] = stVO.getStd_phone(); //수강생 전화번호
            tableData[i][3] = stVO.getStd_email(); //수강생 이메일
            i++;
        }
        // 학생 테이블 비활성화
        stdTable.setModel(new DefaultTableModel(tableData, c_name){
            @Override
            public boolean isCellEditable(int row, int column) {

                return false;
            }
        });
    }

    //인자로 받은 List구조를 2차원 배열로 변환한 후 JTable에 표현!
    private void adminViewTable(List<AdminVO> adList){
        //adminTable에 데이터 세팅
        tableData = new String[adList.size()][a_name.length];
        int i= 0;
        for(AdminVO advo : adList){
            tableData[i][0] = advo.getAdno(); //강사번호
            tableData[i][1] = advo.getAd_name(); //강사이름
            tableData[i][2] = advo.getAd_phone(); //강사 전화번호
            tableData[i][3] = advo.getAd_email(); //강사email
            i++;
        }

        //강사 테이블 비활성화 처리
        adTable.setModel(new DefaultTableModel(tableData, a_name){
            @Override
            public boolean isCellEditable(int row, int column) {

                return false;
            }
        });

    }

    //인자로 받은 List구조를 2차원 배열로 변환한 후 JTable에 표현!
    private void lecViewTable(List<ClassVO> lecList){
        //lecTable에 데이터 세팅
        tableData = new String[lecList.size()][l_name.length];
        int i= 0;
        for(ClassVO lecvo : lecList){
            tableData[i][0] = lecvo.getLec_name(); //강의명
            tableData[i][1] = lecvo.getAd_name();  //담당 강사명
            tableData[i][2] = lecvo.updatePeriod(); //강의 기간
            tableData[i][3] = lecvo.getLec_limit(); //수강 정원
            i++;
        }

        //강의 테이블 비활성화 처리
        lecTable.setModel(new DefaultTableModel(tableData, l_name){
            @Override
            public boolean isCellEditable(int row, int column) {

                return false;
            }
        });

    }



    private void initComponents() {

        jPanel1 = new JPanel();
        jPanel9 = new JPanel();
        jLabel1 = new JLabel();
        jPanel2 = new JPanel();
        jTabbedPane1 = new JTabbedPane();
        jPanel5 = new JPanel();
        jPanel3 = new JPanel();
        jLabel3 = new JLabel();
        jTextField1 = new JTextField();
        stdSearch_bt = new JButton();
        stdDel_bt = new JButton();
        jScrollPane1 = new JScrollPane();
        stdTable = new JTable();
        jPanel8 = new JPanel();
        jPanel11 = new JPanel();
        jLabel6 = new JLabel();
        jTextField2 = new JTextField();
        adSearch_bt = new JButton();
        jButton12 = new JButton();
        jScrollPane4 = new JScrollPane();
        adTable = new JTable();
        jPanel6 = new JPanel();
        jPanel10 = new JPanel();
        jLabel4 = new JLabel();
        jTextField3 = new JTextField();
        lecSearch_bt = new JButton();
        addLec_bt = new JButton();
        jButton8 = new JButton();
        jScrollPane2 = new JScrollPane();
        lecTable = new JTable();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("관리자 페이지");

        GroupLayout jPanel9Layout = new GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
                jPanel9Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                                .addContainerGap(41, Short.MAX_VALUE)
                                .addComponent(jLabel1)
                                .addGap(39, 39, 39))
        );
        jPanel9Layout.setVerticalGroup(
                jPanel9Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                                .addContainerGap(45, Short.MAX_VALUE)
                                .addComponent(jLabel1)
                                .addGap(38, 38, 38))
        );

        jPanel1.add(jPanel9);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel5.setLayout(new java.awt.BorderLayout());

        jLabel3.setText("이름: ");

        jTextField1.setColumns(8);

        stdSearch_bt.setText("검색");

        stdDel_bt.setText("삭제");

        GroupLayout jPanel3Layout = new GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(jLabel3)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(stdSearch_bt) // 검색버튼
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 150, Short.MAX_VALUE)
                                .addComponent(stdDel_bt)
                                .addGap(24, 24, 24))
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3)
                                        .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(stdSearch_bt)
                                        .addComponent(stdDel_bt))
                                .addContainerGap(19, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel3, java.awt.BorderLayout.PAGE_START);

        stdTable.setModel(new DefaultTableModel(tableData, c_name));

        jScrollPane1.setViewportView(stdTable);

        jPanel5.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("수강생 관리", jPanel5);

        jPanel8.setLayout(new java.awt.BorderLayout());

        jLabel6.setText("이름: ");

        jTextField2.setColumns(8);

        adSearch_bt.setText("검색");

        jButton12.setText("삭제");

        GroupLayout jPanel11Layout = new GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
                jPanel11Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(jLabel6)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(adSearch_bt)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 290, Short.MAX_VALUE)
                                .addComponent(jButton12)
                                .addGap(24, 24, 24))
        );
        jPanel11Layout.setVerticalGroup(
                jPanel11Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addGroup(jPanel11Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel6)
                                        .addComponent(jTextField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(adSearch_bt)
                                        .addComponent(jButton12))
                                .addContainerGap(19, Short.MAX_VALUE))
        );

        jPanel8.add(jPanel11, java.awt.BorderLayout.PAGE_START);

        adTable.setModel(new DefaultTableModel(tableData, c_name));

        jScrollPane4.setViewportView(adTable);

        jPanel8.add(jScrollPane4, java.awt.BorderLayout.CENTER);

        //2번째 탭
        jTabbedPane1.addTab("강사관리", jPanel8);

        jPanel6.setLayout(new java.awt.BorderLayout());

        jLabel4.setText("강의명: ");

        jTextField3.setColumns(18);

        lecSearch_bt.setText("검색");

        addLec_bt.setText("추가");

        jButton8.setText("삭제");

        GroupLayout jPanel10Layout = new GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
                jPanel10Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(jLabel4)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lecSearch_bt)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 168, Short.MAX_VALUE)
                                .addComponent(addLec_bt)
                                .addGap(18, 18, 18)
                                .addComponent(jButton8)
                                .addGap(24, 24, 24))
        );
        jPanel10Layout.setVerticalGroup(
                jPanel10Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addGroup(jPanel10Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel4)
                                        .addComponent(jTextField3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lecSearch_bt)
                                        .addComponent(addLec_bt)
                                        .addComponent(jButton8))
                                .addContainerGap(19, Short.MAX_VALUE))
        );

        jPanel6.add(jPanel10, java.awt.BorderLayout.PAGE_START);

        lecTable.setModel(new DefaultTableModel(tableData, l_name));

        jScrollPane2.setViewportView(lecTable);

        jPanel6.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("강의관리", jPanel6);

        jPanel2.add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>





    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>


        // 필요한 경우 dummyMember.setId("...") 등 초기화
        java.awt.EventQueue.invokeLater(() -> new AdminFrame().setVisible(true));
    }

    // Variables declaration - do not modify
    private JButton stdSearch_bt;
    private JButton adSearch_bt;
    private JButton jButton12;
    private JButton lecSearch_bt;
    private JButton addLec_bt;
    private JButton stdDel_bt;
    private JButton jButton8;
    private JLabel jLabel1;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel6;

    private JPanel jPanel1;
    private JPanel jPanel10;
    private JPanel jPanel11;
    private JPanel jPanel2;
    private JPanel jPanel3;
    private JPanel jPanel5;
    private JPanel jPanel6;
    private JPanel jPanel8;
    private JPanel jPanel9;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JScrollPane jScrollPane4;
    private JTabbedPane jTabbedPane1;
    private JTable stdTable;
    private JTable lecTable;
    private JTable adTable;
    private JTextField jTextField1;
    private JTextField jTextField2;
    private JTextField jTextField3;
    // End of variables declaration


    //수강생 관리 데이터 업데이트
    public void stdUpdateData(StudentVO stdVO) {
        SqlSession ss = factory.openSession();

        int cnt = ss.update("student.modify", stdVO);
        if(cnt > 0) {
            ss.commit();

            //JTable값을 갱신한다. 사용자가 JTable에서 더블클릭한
            // 행번호(index)를 알아야 한다.
            stdTable.setValueAt(stdVO.getStd_phone(),i,2);//연락처
            stdTable.setValueAt(stdVO.getStd_email(),i,3);//e-mail
            //멤버변수 stuList의 내용도 변경
            stuList.set(i,stdVO);

        } else  {
            ss.rollback();
        }
        ss.close();
    }

    //강사 관리 데이터 업데이트
    public void adUpdateData(AdminVO adVO) {
        SqlSession ss = factory.openSession();

        int cnt = ss.update("admin.modify", adVO);
        if(cnt > 0) {
            ss.commit();

            //JTable값을 갱신한다. 사용자가 JTable에서 더블클릭한
            // 행번호(index)를 알아야 한다.
            adTable.setValueAt(adVO.getAdno(),i,0);//강의명
            adTable.setValueAt(adVO.getAd_name(),i,1);//강사명
            adTable.setValueAt(adVO.getAd_phone(),i,2);//연락처
            adTable.setValueAt(adVO.getAd_email(),i,3);//e-mail

            //멤버변수 lecList의 내용도 변경
            adList.set(i,adVO);

        } else  {
            ss.rollback();
        }
        ss.close();
    }

    //강의 관리 데이터 업데이트
    public void lecUpdateData(ClassVO cVO) {
        SqlSession ss = factory.openSession();

        int cnt = ss.update("lec_t.modify", cVO);
        if(cnt > 0) {
            ss.commit();

            //JTable값을 갱신한다. 사용자가 JTable에서 더블클릭한
            // 행번호(index)를 알아야 한다.
            lecTable.setValueAt(cVO.getLec_name(),i,0);//강의명
            lecTable.setValueAt(cVO.getAd_name(),i,1);//강사명
            lecTable.setValueAt(cVO.updatePeriod(),i,2);//강의기간
            lecTable.setValueAt(cVO.getLec_limit(),i,3);//정원

            //멤버변수 lecList의 내용도 변경
            lecList.set(i,cVO);

        } else  {
            ss.rollback();
        }
        ss.close();
    }

    //강의 추가
    public void lecInsertData(ClassVO lecVO){
        SqlSession ss = factory.openSession();
        try {
            int cnt = ss.insert("lec_t.insert", lecVO);
            if (cnt > 0) {
                ss.commit();
                allClassData(); //강의 테이블 전체조회
                System.out.println("강의 추가 성공");
            } else {
                ss.rollback();
                System.out.println("강의 추가 실패");
            }
        } catch (Exception e) {
            ss.rollback();
            e.printStackTrace();
        } finally {
            ss.close();
        }
    }

}