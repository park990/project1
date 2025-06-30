//import ClassInfoDialog;
import pm.vo.*;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
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

    AdminVO adminVO;
    MemberVO vo;


    Reader r;


    JMenuBar bar;
    JMenu file_M;
    JMenuItem logOut_item, exit_item;

    int i;
    //박준형0627 선언
    int admin; //0이면 강사, 1이면 관리자
    //박준형0627 선언 끝

    //생성자
    public AdminFrame(MemberVO vo) throws IOException {

        this.vo = vo;
        r = Resources.getResourceAsReader("pm/config/conf.xml");
        factory = new SqlSessionFactoryBuilder().build(r);

        initComponents(); //화면구성

        //회원ID + 환영 글자 출력 선영0628
        jLabelWelcome.setText(vo.getMem_id() + "님 환영합니다.");

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
        //박준형추가부분 시작
        if(vo.getMem_admin_inputOrNot().equals("0")){//그냥 강사일 경우
            jTabbedPane1.setEnabledAt(0,false);
            jTabbedPane1.setEnabledAt(1,false);
            //메서드 하나 만듦
            mylec();
            jTabbedPane1.setSelectedIndex(2);
        }else {
            admin =1;
            allStudentData();
        }

        stdDel_bt.addActionListener(new ActionListener() { //학생 삭제 버튼을 눌렀을 때
            @Override
            public void actionPerformed(ActionEvent e) {
                SqlSession ss = factory.openSession();
                //member.xml에 내용 추가
                int std_update_cnt = ss.update("member.std_disable",stuList.get(stdTable.getSelectedRow()).getStdno());

                if(std_update_cnt >0){//회원 정보 변경
                    ss.commit();
                }else {
                    ss.rollback();
                }
                ss.close();
                allStudentData();
            }
        });
        jButton12.addActionListener(new ActionListener() { // 강사 삭제 버튼을 눌렀을 때
            @Override
            public void actionPerformed(ActionEvent e) {
                SqlSession ss = factory.openSession();
                //member.xml에 내용 추가
                int ad_update_cnt = ss.update("member.ad_disable",adList.get(adTable.getSelectedRow()).getAdno());

                if(ad_update_cnt >0){//회원 정보 변경
                    ss.commit();
                }else {
                    ss.rollback();
                }
                ss.close();
                allAdminData();
            }
        });
        jButton8.addActionListener(new ActionListener() { // 강의 삭제 버튼을 눌렀을 때
            @Override
            public void actionPerformed(ActionEvent e) {
                SqlSession ss = factory.openSession();
                //lec.xml에 내용 추가
                int lec_del_cnt = ss.delete("lec.del_lec",lecList.get(lecTable.getSelectedRow()).getLec_no());

                if(lec_del_cnt >0){//회원 정보 변경
                    ss.commit();
                }else {
                    ss.rollback();
                }
                ss.close();
                if(admin ==0) {
                    mylec();
                }else{
                    allClassData();
                }
            }
        });
        //박준형추가부분 끝
        SqlSession ss = factory.openSession();
        adminVO = ss.selectOne("admin.get_no", vo.getAdno());
        //System.out.println("어드민::::::::" + adminVO.toString());

        //일반강사(회원가입 시 admin을 입력하지 않은 강사)일 경우에 수강생탭, 강사탭 비활성화 선영수정
//        if(adminVO != null && !adminVO.getMem_admin_inputOrNot().equals("1")){
//            jTabbedPane1.setEnabledAt(0, false);
//            jTabbedPane1.setEnabledAt(1, false);
//            jTabbedPane1.setSelectedIndex(2);
//            allClassData();
//        }

        //allStudentData();

        //이벤트 감지자 등록
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0); //프로그램 종료
            }
        });

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
                    //System.out.println(stVO);
                    new StudentInfoDialog(AdminFrame.this, true, stVO);

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

        //강의관리 탭 - 검색 버튼 클릭 시 선영0628
        lecSearch_bt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //사용자가 입력한 값 추출
                String lecName = jTextField3.getText().trim();
                SqlSession ss = factory.openSession();
                if (vo.getMem_admin_inputOrNot().equals(1)){ //총괄 강사가 서치하는 경우
                    if (lecName.length() > 0) {
                        lecList = ss.selectList("lec_t.lec_search", lecName);
                        lecViewTable(lecList);
                    } else {
                        //입력을 안하고 검색 클릭 시
                        //JOptionPane.showMessageDialog(AdminFrame.this, "강의명을 입력 후 검색버튼을 눌러주세요.");
                        lecList = ss.selectList("lec_t.all", lecName); //전체 리스트 출력
                        lecViewTable(lecList);
                    }
                } else{ //일반 강사가 서치하는 경우
                    if (lecName.length() > 0) {
                        lecList = ss.selectList("lec_t.myLec_search", lecName);
                        lecViewTable(lecList);
                    } else {
                        //입력을 안하고 검색 클릭 시
                        mylec(); //자신의 강의 전체를 출력
                    }

                }
                ss.close();
            }
        });

        //강의관리 추가 버튼 클릭 시
        addLec_bt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddLecDialog(AdminFrame.this, true);
            }
        });

        //종료 클릭 시
        exit_item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        //로그아웃 클릭 시
        logOut_item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Frame1();
                dispose();
            }
        });

        ss.close();
    } //생성자 끝

    //박준형0627추가 메서드
    private void mylec(){
        SqlSession ss = factory.openSession();
        lecList = ss.selectList("lec_t.mylec",vo.getAdno());
        ss.close();
        lecViewTable(lecList);
    }

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


    //수강생 관리 데이터 업데이트
    public void stdUpdateData(StudentVO stdVO, MemberVO vo) {
        SqlSession ss = factory.openSession();

        int cnt = ss.update("student.modify", stdVO);

        if(cnt > 0) { //실행을 해서 결과가 있는 경우
            ss.update("member.update_mem_t" ,vo);
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
    public void lecUpdateData(ClassVO cVO, EnrolledStudentVO enVO) {
        SqlSession ss = factory.openSession();

        int cnt = ss.update("lec_t.modify", cVO);
        if(cnt > 0) {
            ss.update("lec_std_t.modify", enVO);
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
                //박준형0627 추가
                if(admin==1) {
                    allClassData(); //강의 테이블 전체조회
                }else{
                    mylec();

                }
                //박준형0627 추가 끝
                System.out.println("강의 추가 성공");
                int ad_insert_cnt = ss.insert("admin.insert_rec_adm",lecVO);
                if(ad_insert_cnt>0){
                    ss.commit();
                }else{
                    ss.rollback();
                }
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

        //--------------로고 이미지 추가 및 회원ID + 환영 글자 출력 선영0629

        //로고 이미지 추가
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/img.png"));
        Image img = icon.getImage();
        Image resized = img.getScaledInstance(130, 130, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resized);

        // 라벨 분리: 로고, 제목, 환영 메시지
        JLabel jLabelLogo = new JLabel(resizedIcon); // 로고만
        JLabel jLabelTitle = new JLabel("관리자 페이지");
        jLabelTitle.setFont(new Font("맑은 고딕", Font.BOLD, 16));

        jLabelWelcome = new JLabel("***님 환영합니다.");
        jLabelWelcome.setFont(new Font("맑은 고딕", Font.PLAIN, 14));

        // 관리자페이지 글씨, 환영 메시지를 수직으로 정렬
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false); // 투명 배경
        textPanel.add(jLabelTitle);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(jLabelWelcome);

        // 로고 + 텍스트를 나란히 정렬하는 메인 헤더 패널
        jPanel9.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15)); // 좌측 정렬
        jPanel9.add(jLabelLogo);
        jPanel9.add(textPanel);

        //---------------------------------------------------------------------------------

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
    private JLabel jLabelWelcome; //로그인 한 사람을 표시해주는 라벨
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

}