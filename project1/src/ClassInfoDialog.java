import pm.vo.*;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Reader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ClassInfoDialog extends JDialog {

    AdminFrame parent;
    CardLayout cardLayout;

    String[] std_name = {"이름", "수강제목", "연락처"};
    String[] t_name = {"시험명", "총 문제수"};

    String[][] tableData;

    SqlSessionFactory factory;
    List<EnrolledStudentVO> esList;
    List<TestVO> testList;

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ClassInfoDialog.class.getName());

    public ClassInfoDialog(AdminFrame parent, boolean modal, ClassVO cVO) {
        super(parent, modal);


        //cardLayout 초기화
        cardLayout = new CardLayout();

        initComponents(); //화면구성
        init(); //db연결
        this.setTitle("강의 상세정보");// 재윤** 이거 initcomponets();보다 위로 가있어서 적용이 안됐음 그래서 아래로 보냄



        //강의정보 db 연동하여 출력
        className_tf.setText(cVO.getLec_name());
        classAdmin_tf.setText(cVO.getAd_name());
        classSdate_tf.setText(cVO.getLec_sdate());
        classEdate_tf.setText(cVO.getLec_edate());
        classLimit_tf.setText(cVO.getLec_limit());
        classInfo_ta.setText(cVO.getLec_info());

        //텍스트필드 비활성화 처리
        className_tf.setEditable(false);
        classAdmin_tf.setEditable(false);
        classSdate_tf.setEditable(false);
        classEdate_tf.setEditable(false);
        classLimit_tf.setEditable(false);
        classInfo_ta.setEditable(false);

        //저장버튼 비활성화
        cInfoSave_bt.setEnabled(false);

        //강의 정보 수정 버튼 클릭 시
        cInfoMod_bt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //수정 가능한 텍스트필드 활성화
                className_tf.setEditable(true);
                classAdmin_tf.setEditable(true);
                classLimit_tf.setEditable(true);
                classSdate_tf.setEditable(true);
                classEdate_tf.setEditable(true);
                classInfo_ta.setEditable(true);

                //버튼 활성화 구분 처리
                cInfoMod_bt.setEnabled(false);
                cInfoSave_bt.setEnabled(true);
            }
        });

        //강의 정보 수정 후 저장 버튼 클릭 시
        cInfoSave_bt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //입력한 값을 공백제거하여 변수에 저장
                String lec_name = className_tf.getText().trim();
                String ad_name = classAdmin_tf.getText().trim();
                String lec_sdate = classSdate_tf.getText().trim(); //강의시작일
                String lec_edate = classEdate_tf.getText().trim(); //강의종료일
                String lec_limit = classLimit_tf.getText().trim();
                String lec_info = classInfo_ta.getText().trim();

                //강사명으로 강사번호 조회
                SqlSession ss = factory.openSession();
                //String adno = ss.selectOne("admin_t.admin_search", ad_name);
                AdminVO avo = ss.selectOne("admin.admin_search", ad_name);
                ss.close();
                //강사번호가 null인 경우, 창띄우기
                if(avo == null){
                    JOptionPane.showMessageDialog(ClassInfoDialog.this, "담당 강사가 없습니다." + ad_name + " 오류");
                    return;
                }

                //입력하는 날짜 유효성 체크
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                try {
                    LocalDate start = LocalDate.parse(lec_sdate, formatter);
                    LocalDate end = LocalDate.parse(lec_edate, formatter);

                    // 날짜 순서 확인
                    if (start.isAfter(end)) {
                        JOptionPane.showMessageDialog(ClassInfoDialog.this,
                                "강의 시작일은 종료일보다 빠르거나 같아야 합니다.",
                                "날짜 순서 오류",
                                JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(ClassInfoDialog.this,
                            "날짜는 yyyy-MM-dd 형식이며 실제 존재하는 날짜여야 합니다.\n예: 2025-06-24",
                            "날짜 유효성 오류",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                //위에 입력한 정보들을 ClassVO에 저장
                ClassVO lecVO = new ClassVO();
                lecVO.setLec_no(cVO.getLec_no()); //강의에 대한 강의번호
                lecVO.setLec_name(lec_name); //강의명
                lecVO.setAd_name(ad_name); //강사명
                lecVO.setAdno(avo.getAdno()); //강사의 강사번호
                lecVO.setLec_sdate(lec_sdate); //강의시작일
                lecVO.setLec_edate(lec_edate); //강의종료일
                lecVO.setLec_limit(lec_limit); //정원
                lecVO.setLec_info(lec_info); //강의정보

                try{
                    parent.lecUpdateData(lecVO);
                    JOptionPane.showMessageDialog(ClassInfoDialog.this, "강의 정보 수정이 완료되었습니다.");
                }catch (Exception ex){
                    JOptionPane.showMessageDialog(ClassInfoDialog.this, "수정 중 오류가 발생하였습니다.:\n" + ex.getMessage());
                    ex.printStackTrace(); // 콘솔에 에러 로그 출력 (디버깅용)
                }

                //텍스트필드 비활성화
                className_tf.setEditable(false);
                classAdmin_tf.setEditable(false);
                classSdate_tf.setEditable(false);
                classEdate_tf.setEditable(false);
                classLimit_tf.setEditable(false);
                classInfo_ta.setEditable(false);

                //버튼 활성화 구분 처리
                cInfoMod_bt.setEnabled(true);
                cInfoSave_bt.setEnabled(false);
            }
        });

        //취소 버튼 클릭 시
        cInfoCancle_bt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //취소 클릭 시 어떻게 할 것인지?
                dispose();
            }
        });


        //수강생 조회 버튼 클릭 시
        stdSelect_bt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println("수강생 조회");

                Map<String, Object> map = new HashMap<>();
                map.put("lec_no", cVO.getLec_no());

                SqlSession ss = factory.openSession();
                esList = ss.selectList("lec_std_t.enrolledStudent", map);
                ss.close();
                enrolledStudentTable(esList); //수강중인 학생 테이블 출력하는 함수 enrolledStudentTable 호출


                cardLayout.show(infoTable, "studentCard");
            }
        });

        //시험 조회 버튼 클릭 시
        tSelect_bt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println("시험 조회");

                Map<String, Object>map = new HashMap<>();
                map.put("lec_no", cVO.getLec_no());

                SqlSession ss = factory.openSession();
                testList = ss.selectList("test_t.selectTest", map);
                ss.close();
                testViewTable(testList); //시험 목록 테이블 출력하는 함수 testViewTable 호출

                //카드레이아웃 화면 전환
                cardLayout.show(infoTable, "examCard");
            }
        });
        // 시험 추가 버튼을 눌렀을 때 - 박준형 시작

        tAdd_bt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField t_nameField = new JTextField();
                JTextField qnumField = new JTextField();
                boolean retryAdd = true;
                Object[] message = {
                        "시험명:", t_nameField,
                        "총문제수:", qnumField
                };
                int nd_cnt = 0; // 입력하지 않은 칸이 있는 지 확인하는 용도
                while(retryAdd){
                    String title = new String();
                    if(nd_cnt==0){ // 첫번째 시도
                        title = "시험 정보 입력";
                    }else{ // 이 이후 시도
                        title = "시험 정보를 빈 칸 없이 모두 채워주세요";
                    }

                    nd_cnt =0;
                    int option = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.OK_CANCEL_OPTION);// Dialog 세팅

                    if (option == JOptionPane.OK_OPTION) { // 확인 버튼을 눌렀을 때
                        String t_name = t_nameField.getText().trim();
                        String qnum = qnumField.getText().trim();

//                        StringBuffer sb = new StringBuffer();
                        if (t_name.length() > 0) {
                            System.out.println(t_name);
                        }else{
                            nd_cnt++;

                        }
                        if (qnum.length() > 0) {
                            System.out.println(qnum);
                        }else{
                            nd_cnt++;

                        }
                        if (nd_cnt == 0) {// 모든 칸에 다 입력을 했을 때
                            SqlSession ss= factory.openSession();
                            TestVO tvo = new TestVO();



                            tvo.setLec_no(cVO.getLec_no());
                            tvo.setTest_name(t_nameField.getText());
                            tvo.setTest_ques_num(qnumField.getText());

                            int insert_cnt = ss.insert("test_t.save",tvo);

                            if(insert_cnt>0){
                                ss.commit();
                            }else {
                                ss.rollback();
                            }
                            ss.close();
                            retryAdd = false;
                            // 데이터 베이스에 저장하는 문장을 삽입

                        }// 모든 칸에 다 입력했을 때 끝

                    }//Dialog에서 확인 버튼을 눌렀을 때 끝
                    else{// Dialog에서 확인 버튼 이외의 다른 버튼을 눌렀을 때
                        retryAdd = false;
                    }

                }


            }
        });
        //시험 삭제 버튼을 눌렀을 때
        tDelete_bt.addActionListener(new ActionListener() {//시험 삭제 버튼을 눌렀을 때
            @Override
            public void actionPerformed(ActionEvent e) {
                //선택된 시험의 인덱스 받아오기
                int i = jTable4.getSelectedRow();
                //시험 받기
                TestVO tvo = testList.get(i);
                SqlSession ss = factory.openSession();
                //해당 시험에 대한 답안 다 지우기
                int anw_del_cnt = ss.delete("test_anw.del_allanw",tvo.getTest_idx());
                //해당 시험에 대한 문항 다 지우기
                //해당 시험에 대한 문항 다 지우기 1. 문제 다 받아오기
                List<test_quesVO> t_del_qlist =  ss.selectList("test_ques.exam_ques",tvo.getTest_idx());
                //해당 시험에 대한 문항 다 지우기 2. 문항 제거 하기
                int item_del_cnt=0;
                for(test_quesVO qvo :t_del_qlist) {
                    int cnt = ss.delete("test_item.del_items", qvo);
                    if(cnt>0){
                        item_del_cnt++;
                    }
                }
                //해당 시험에 대한 문제 다 지우기
                int ques_del_cnt = ss.delete("test_ques.del_allques",tvo.getTest_idx());
                //해당 시험 지우기
                int test_del_cnt =ss.delete("test_t.del_test",tvo.getTest_idx());

                //commit을 해도 되는가 비교

                if(anw_del_cnt>0||item_del_cnt>0||ques_del_cnt>0||test_del_cnt>0){
                    ss.commit();
                }else {
                    ss.rollback();
                }

                ss.close();
            }
        });

        // 시험 테이블에서 더블 클릭으로 문제 만들기 or 문제 수정하기
        jTable4.addMouseListener(new MouseAdapter() { // 시험 테이블에 더블 클릭했을때
            @Override
            public void mouseClicked(MouseEvent e) {
                int cnt = e.getClickCount();
                if(cnt == 2){ //더블 클릭 시
                    //jTable1에서 선택된 행, index를 얻어내자
                    int i = jTable4.getSelectedRow();
                    TestVO tvo = testList.get(i); //시험 선택
                    //문제 받아오기
                    SqlSession ss = factory.openSession();
                    List<test_quesVO> list = ss.selectList("test_ques.exam_ques",tvo.getTest_idx());
                    //System.out.println(list.size());//잘들어오는지 확인용
                    //System.out.printf("testList길이:%d,test_idx:%s,lec_no:%s,test_name:%s",testList.size(),tvo.getTest_idx(),tvo.getLec_no(),tvo.getTest_name()); //잘들어오는지 확인

                    if(list.size()==0){//시험은 만들고 문제를 하나도 생성하지 않았을 때
                        new addTest(tvo,1,true,list);
                    }else if(list.size()< Integer.parseInt(tvo.getTest_ques_num())){ // 시험을 만들고 문제를 저장하다가 말았을 때
                        new addTest(tvo,list.size()+1,true,list);
                    }else{ // 시험을 만들고 문제를 다 넣었을 때
                        new addTest(tvo,list.size(),false,list);
                    }



                }
            }
        });
        // 박준형 끝

        //창 띄우자 마자 수강중인 학생 출력
        Map<String, Object> map = new HashMap<>();
        map.put("lec_no", cVO.getLec_no());

        SqlSession ss = factory.openSession();
        esList = ss.selectList("lec_std_t.enrolledStudent", map);
        ss.close();
        enrolledStudentTable(esList); //수강중인 학생 테이블 출력하는 함수 enrolledStudentTable 호출


        this.setBounds(100, 20, 830, 800); //창 크기
        this.setVisible(true); //창 띄우기
    }

    //수강중인 학생 테이블 출력
    private void enrolledStudentTable(List<EnrolledStudentVO> esList){

        tableData = new String[esList.size()][std_name.length];
        int i = 0;
        for(EnrolledStudentVO esVO : esList){
            tableData[i][0] = esVO.getStd_name(); //수강생 이름 출력
            tableData[i][1] = esVO.getLec_name(); //강의명
            tableData[i][2] = esVO.getStd_phone(); //수강생 연락처
            i++;
        }

        // 학생 테이블 비활성화
        jTable2.setModel(new DefaultTableModel(tableData, std_name){
            @Override
            public boolean isCellEditable(int row, int column) {

                return false;
            }
        });
        jTable2.revalidate();
        jTable2.repaint();
    }

    //시험 목록 출력
    public void testViewTable(List<TestVO> testList){
        System.out.println("시험 목록 출력");

        /*if(testList == null || testList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "시험 데이터가 없습니다.");
            return;
        }*/

        tableData = new String[testList.size()][t_name.length];
        int j = 0;
        for(TestVO testVO : testList){
            /*if(testVO == null) {
                continue;
            }*/
            tableData[j][0] = testVO.getTest_name(); //시험 이름
            tableData[j][1] = String.valueOf(testVO.getTest_ques_num()); //문제수
            j++;
        }

        //시험 테이블 비활성화 처리
        jTable4.setModel(new DefaultTableModel(tableData, t_name){
            @Override
            public boolean isCellEditable(int row, int column) {

                return false;
            }
        });
        jTable4.revalidate();
        jTable4.repaint();
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

    private void initComponents() {
        GridBagConstraints gridBagConstraints;

        jPanel1 = new JPanel();
        rowPanel1 = new JPanel();
        classInfoMod = new JPanel();
        classInfoTitle = new JPanel();
        jLabel1 = new JLabel();
        jPanel3 = new JPanel();
        jLabel2 = new JLabel();
        className_tf = new JTextField();
        jPanel8 = new JPanel();
        jLabel3 = new JLabel();
        classAdmin_tf = new JTextField();
        jPanel9 = new JPanel();
        jLabel4 = new JLabel();
        classSdate_tf = new JTextField(10);
        classEdate_tf = new JTextField(10);
        jPanel10 = new JPanel();
        jLabel5 = new JLabel();
        classLimit_tf = new JTextField();
        jPanel11 = new JPanel();
        jPanel6 = new JPanel();
        jPanel7 = new JPanel();
        cInfoMod_bt = new JButton();
        cInfoSave_bt = new JButton();
        cInfoCancle_bt = new JButton();
        classDescInfo = new JPanel();
        jPanel2 = new JPanel();
        jLabel6 = new JLabel();
        tilLabel = new JLabel(" ~ ");
        jPanel12 = new JPanel();
        jScrollPane1 = new JScrollPane();
        classInfo_ta = new JTextArea();
        rowPanel2 = new JPanel();
        infoTable = new JPanel();
        stdTable = new JPanel(cardLayout);
        jPanel13 = new JPanel();
        jPanel14 = new JPanel();
        jPanel4 = new JPanel();
        jLabel7 = new JLabel();
        jLabel9 = new JLabel();
        jPanel5 = new JPanel();
        jPanel21 = new JPanel();
        jScrollPane3 = new JScrollPane();
        jTable2 = new JTable();
        jPanel22 = new JPanel();
        testTable = new JPanel(cardLayout);
        jScrollPane5 = new JScrollPane();
        jTable4 = new JTable();
        classButton = new JPanel();
        jPanel27 = new JPanel();
        jPanel28 = new JPanel();
        jPanel29 = new JPanel();
        jPanel30 = new JPanel();
        jPanel31 = new JPanel();
        tSelect_bt = new JButton();
        tDelete_bt = new JButton();
        tAdd_bt = new JButton();
        stdAdd_bt = new JButton();
        stdSelect_bt = new JButton();
        stdDelete_bt = new JButton();

        GroupLayout jPanel9Layout = new GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setLayout(new GridBagLayout());

        rowPanel1.setLayout(new GridLayout(0, 2));

        classInfoMod.setLayout(new GridLayout(6, 0));

        jLabel1.setFont(new Font("맑은 고딕", 1, 14));
        jLabel1.setText("강의 정보");

        GroupLayout classInfoTitleLayout = new GroupLayout(classInfoTitle);
        classInfoTitle.setLayout(classInfoTitleLayout);
        classInfoTitleLayout.setHorizontalGroup(
            classInfoTitleLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(classInfoTitleLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel1)
                .addContainerGap(360, Short.MAX_VALUE))
        );
        classInfoTitleLayout.setVerticalGroup(
            classInfoTitleLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, classInfoTitleLayout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

        classInfoMod.add(classInfoTitle);

        jLabel2.setFont(new Font("맑은 고딕", 0, 14)); // NOI18N
        jLabel2.setText("강의명: ");

        className_tf.setFont(new Font("맑은 고딕", 0, 14)); // NOI18N
        className_tf.setText("className_tf");

        GroupLayout jPanel3Layout = new GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel2)
                .addGap(26, 26, 26)
                .addComponent(className_tf, GroupLayout.PREFERRED_SIZE, 291, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(className_tf, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        classInfoMod.add(jPanel3);

        jLabel3.setFont(new Font("맑은 고딕", 0, 14)); // NOI18N
        jLabel3.setText("강사명:");

        classAdmin_tf.setFont(new Font("맑은 고딕", 0, 14)); // NOI18N
        classAdmin_tf.setText("className_tf");

        GroupLayout jPanel8Layout = new GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel3, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(classAdmin_tf, GroupLayout.PREFERRED_SIZE, 290, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(53, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel8Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(classAdmin_tf, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        classInfoMod.add(jPanel8);

        jLabel4.setFont(new Font("맑은 고딕", 0, 14)); // NOI18N
        jLabel4.setText("강의기간:");

        classSdate_tf.setFont(new Font("맑은 고딕", 0, 14)); // NOI18N
        classEdate_tf.setFont(new Font("맑은 고딕", 0, 14)); // NOI18N
        //classSdate_tf.setText("className_tf");

        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                            .addGap(34, 34, 34)
                            .addComponent(jLabel4, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(classSdate_tf, GroupLayout.PREFERRED_SIZE, 133, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(tilLabel)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(classEdate_tf, GroupLayout.PREFERRED_SIZE, 133, GroupLayout.PREFERRED_SIZE)
                            .addContainerGap(56, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                            .addGap(23, 23, 23)
                            .addGroup(jPanel9Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(classSdate_tf, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tilLabel)
                                    .addComponent(classEdate_tf, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        classInfoMod.add(jPanel9);

        jLabel5.setFont(new Font("맑은 고딕", 0, 14)); // NOI18N
        jLabel5.setText("정원:");

        classLimit_tf.setFont(new Font("맑은 고딕", 0, 14)); // NOI18N
        classLimit_tf.setText("className_tf");

        GroupLayout jPanel10Layout = new GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel5, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(classLimit_tf, GroupLayout.PREFERRED_SIZE, 291, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(55, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel10Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(classLimit_tf, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        classInfoMod.add(jPanel10);

        jPanel11.setLayout(new BorderLayout());

        jPanel6.setPreferredSize(new Dimension(130, 59));

        GroupLayout jPanel6Layout = new GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 130, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 59, Short.MAX_VALUE)
        );

        jPanel11.add(jPanel6, BorderLayout.LINE_START);

        cInfoMod_bt.setText("수정");

        cInfoSave_bt.setText("저장");

        cInfoCancle_bt.setText("취소");

        GroupLayout jPanel7Layout = new GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addComponent(cInfoMod_bt)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cInfoSave_bt)
                .addGap(12, 12, 12)
                .addComponent(cInfoCancle_bt)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(cInfoMod_bt, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
                    .addComponent(cInfoSave_bt, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
                    .addComponent(cInfoCancle_bt, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel11.add(jPanel7, BorderLayout.CENTER);

        classInfoMod.add(jPanel11);

        rowPanel1.add(classInfoMod);

        classDescInfo.setLayout(new BorderLayout());

        jPanel2.setPreferredSize(new Dimension(408, 59));

        jLabel6.setFont(new Font("맑은 고딕", 1, 14)); // NOI18N
        jLabel6.setText("상세 설명");

        GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel6)
                .addContainerGap(360, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addComponent(jLabel6, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

        classDescInfo.add(jPanel2, BorderLayout.PAGE_START);

        classInfo_ta.setColumns(20);
        classInfo_ta.setRows(5);
        jScrollPane1.setViewportView(classInfo_ta);

        GroupLayout jPanel12Layout = new GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 348, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(78, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 262, GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        classDescInfo.add(jPanel12, BorderLayout.CENTER);

        rowPanel1.add(classDescInfo);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.2;
        jPanel1.add(rowPanel1, gridBagConstraints);

        rowPanel2.setLayout(new GridBagLayout());

        infoTable.setLayout(cardLayout);

        stdTable.setLayout(new BorderLayout());

        jLabel7.setFont(new Font("맑은 고딕", 1, 14)); // NOI18N
        jLabel7.setText("수강중인 학생");

        GroupLayout jPanel4Layout = new GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel7)
                .addContainerGap(590, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(29, Short.MAX_VALUE)
                .addComponent(jLabel7, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

        stdTable.add(jPanel4, BorderLayout.PAGE_START);

        jPanel5.setLayout(new BorderLayout());

        //jTable2.setModel(new DefaultTableModel(tableData, std_name));
        jScrollPane3.setViewportView(jTable2);

        GroupLayout jPanel21Layout = new GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jScrollPane3, GroupLayout.PREFERRED_SIZE, 522, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jScrollPane3, GroupLayout.PREFERRED_SIZE, 279, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel21, BorderLayout.CENTER);

        jPanel22.setPreferredSize(new Dimension(595, 20));

        GroupLayout jPanel22Layout = new GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 712, Short.MAX_VALUE)
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jPanel5.add(jPanel22, BorderLayout.SOUTH);

        stdTable.add(jPanel5, BorderLayout.CENTER);

        infoTable.add(stdTable, "studentCard");

        testTable.setLayout(new BorderLayout());

        jLabel9.setFont(new Font("맑은 고딕", 1, 14)); // NOI18N
        jLabel9.setText("시험");

        GroupLayout jPanel13Layout = new GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
                jPanel13Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel13Layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addComponent(jLabel9)
                                .addContainerGap(590, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
                jPanel13Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                                .addContainerGap(29, Short.MAX_VALUE)
                                .addComponent(jLabel9, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18))
        );

        testTable.add(jPanel13, BorderLayout.PAGE_START);

        jPanel14.setLayout(new BorderLayout());

        jScrollPane5.setViewportView(jTable4);

        GroupLayout jPanel30Layout = new GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
                jPanel30Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel30Layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(jScrollPane5, GroupLayout.PREFERRED_SIZE, 522, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(160, Short.MAX_VALUE))
        );
        jPanel30Layout.setVerticalGroup(
                jPanel30Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel30Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jScrollPane5, GroupLayout.PREFERRED_SIZE, 279, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel14.add(jPanel30, BorderLayout.CENTER);

        jPanel31.setPreferredSize(new Dimension(595, 20));

        GroupLayout jPanel31Layout = new GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
                jPanel31Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 712, Short.MAX_VALUE)
        );
        jPanel31Layout.setVerticalGroup(
                jPanel31Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 20, Short.MAX_VALUE)
        );

        jPanel14.add(jPanel31, BorderLayout.SOUTH);

        testTable.add(jPanel14, BorderLayout.CENTER);

        infoTable.add(testTable, "examCard");

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 2.0;
        gridBagConstraints.weighty = 1.0;
        rowPanel2.add(infoTable, gridBagConstraints);

        classButton.setLayout(new BorderLayout());

        jPanel27.setPreferredSize(new Dimension(193, 65));

        GroupLayout jPanel27Layout = new GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
                jPanel27Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 194, Short.MAX_VALUE)
        );
        jPanel27Layout.setVerticalGroup(
                jPanel27Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 65, Short.MAX_VALUE)
        );

        classButton.add(jPanel27, BorderLayout.PAGE_START);

        jPanel28.setPreferredSize(new Dimension(193, 20));

        GroupLayout jPanel28Layout = new GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
                jPanel28Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 194, Short.MAX_VALUE)
        );
        jPanel28Layout.setVerticalGroup(
                jPanel28Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 20, Short.MAX_VALUE)
        );

        classButton.add(jPanel28, BorderLayout.PAGE_END);

        tSelect_bt.setText("시험 조회");

        tDelete_bt.setText("시험 삭제");

        tAdd_bt.setText("시험 추가");

        stdAdd_bt.setText("수강생 추가");

        stdSelect_bt.setText("수강생 조회");

        stdDelete_bt.setText("수강생 삭제");

        GroupLayout jPanel29Layout = new GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel29Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(tDelete_bt, GroupLayout.PREFERRED_SIZE, 131, GroupLayout.PREFERRED_SIZE)
                    .addComponent(tSelect_bt, GroupLayout.PREFERRED_SIZE, 131, GroupLayout.PREFERRED_SIZE)
                    .addComponent(tAdd_bt, GroupLayout.PREFERRED_SIZE, 131, GroupLayout.PREFERRED_SIZE)
                    .addComponent(stdSelect_bt, GroupLayout.PREFERRED_SIZE, 131, GroupLayout.PREFERRED_SIZE)
                    .addComponent(stdAdd_bt, GroupLayout.PREFERRED_SIZE, 131, GroupLayout.PREFERRED_SIZE)
                    .addComponent(stdDelete_bt, GroupLayout.PREFERRED_SIZE, 131, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(56, Short.MAX_VALUE))
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(stdSelect_bt, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(stdAdd_bt, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(stdDelete_bt, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(tSelect_bt, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tAdd_bt, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(tDelete_bt, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(135, Short.MAX_VALUE))
        );

        classButton.add(jPanel29, BorderLayout.CENTER);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridheight = GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        rowPanel2.add(classButton, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(rowPanel2, gridBagConstraints);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 880, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton cInfoCancle_bt;
    private JButton cInfoMod_bt;
    private JButton cInfoSave_bt;
    private JPanel classButton;
    private JPanel classDescInfo;
    private JPanel classInfoMod;
    private JPanel classInfoTitle;
    private JPanel infoTable;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel tilLabel;
    private JLabel jLabel7;
    private JLabel jLabel9;
    private JPanel jPanel1;
    private JPanel jPanel10;
    private JPanel jPanel11;
    private JPanel jPanel12;
    private JPanel jPanel13;
    private JPanel jPanel14;
    private JPanel jPanel2;
    private JPanel jPanel21;
    private JPanel jPanel22;
    private JPanel jPanel27;
    private JPanel jPanel28;
    private JPanel jPanel29;
    private JPanel jPanel30;
    private JPanel jPanel31;
    private JPanel jPanel3;
    private JPanel jPanel4;
    private JPanel jPanel5;
    private JPanel jPanel6;
    private JPanel jPanel7;
    private JPanel jPanel8;
    private JPanel jPanel9;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane3;
    private JScrollPane jScrollPane5;
    private JTable jTable2;
    private JTable jTable4;
    private JTextArea classInfo_ta;
    private JTextField className_tf;
    private JTextField classAdmin_tf;
    private JTextField classSdate_tf;
    private JTextField classEdate_tf;
    private JTextField classLimit_tf;
    private JPanel rowPanel1;
    private JPanel rowPanel2;
    private JButton stdAdd_bt;
    private JButton stdDelete_bt;
    private JButton stdSelect_bt;
    private JPanel stdTable;
    private JButton tAdd_bt;
    private JButton tDelete_bt;
    private JPanel testTable;
    private JButton tSelect_bt;
    // End of variables declaration//GEN-END:variables
}
