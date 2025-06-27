import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import pm.vo.ClassVO;
import pm.vo.EnrolledStudentVO;
import pm.vo.StudentVO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

//선영수정 파일 추가함

public class AddStdToClass extends JDialog {

    String[] c_name = {"학번", "이름", "연락처"};
    String[][] tableData;

    SqlSessionFactory factory;

    List<EnrolledStudentVO> esList;
    List<StudentVO> stdList;
    //ClassVO lecVO;

    StudentVO stdVO;
    EnrolledStudentVO esVO;
    List<EnrolledStudentVO> estdList; //여러 명의 학생을 담는 멤버변수

    //생성자
    public AddStdToClass(ClassInfoDialog parent, boolean modal, ClassVO cVO) {
        super(parent, modal);
        initComponents();

        init(); //db연결

        esList = parent.enrolledStudent(cVO);
        //System.out.println("앙: " + estdList.toString());
        enrolledStudentTable(esList);
        aVailableStd(cVO); //전체학생

        //추가버튼 클릭 시
        jButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectRow = jTable1.getSelectedRow();
                StudentVO stdVO =  stdList.get(selectRow);
                stdList.remove(selectRow); //선택한 학생 테이블에서 삭제
                selectAvailableStdTable(stdList); //테이블 추출 함수 호출

                esVO = new EnrolledStudentVO(); //수강중인 학생에 대한 VO 빈 깡통
                esVO.setStdno(stdVO.getStdno()); //VO에 값 저장
                esVO.setStd_name(stdVO.getStd_name());
                esVO.setStd_phone(stdVO.getStd_phone());
                esVO.setLec_no(cVO.getLec_no());
                esVO.setLec_name(cVO.getLec_name());
                esVO.setLec_stdno(cVO.getLec_stdno());
                System.out.println(esVO.getLec_stdno());
                esList.add(esVO); //esList에 저장한 VO를 추가

                enrolledStudentTable(esList); //테이블 추출 함수 호출

            }
        });

        //확인버튼 클릭 시
        //여러명이 들어갈 수 있기 때문에 arrayList에 담는다.
        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                estdList = new ArrayList<EnrolledStudentVO>();
                estdList.addAll(esList);

                System.out.println("전부 출력" + estdList);

                SqlSession ss = factory.openSession();
                ss.insert("lec_std_t.insertEnrolledStudent", esVO);
                ss.commit();
                ss.close();

                parent.enrolledStudentTable(estdList);
                dispose();
            }
        });


        //제외버튼 클릭 시
        jButton4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectRow = jTable2.getSelectedRow();

                EnrolledStudentVO esVO =  esList.get(selectRow);
                esList.remove(selectRow);
                enrolledStudentTable(esList);

                stdVO = new StudentVO();
                stdVO.setStdno(esVO.getStdno());
                stdVO.setStd_name(esVO.getStd_name());
                stdVO.setStd_phone(esVO.getStd_phone());
                stdVO.setLec_no(cVO.getLec_no());
                stdVO.setLec_name(cVO.getLec_name());
                stdVO.setLec_stdno(cVO.getLec_stdno());
                System.out.println("제외" + esVO.getLec_stdno());
                stdList.add(stdVO); //stdList에 저장한 VO를 추가

                selectAvailableStdTable(stdList); //테이블 추출 함수 호출

            }
        });

        //확인버튼 클릭 시
        //여러명이 들어갈 수 있기 때문에 arrayList에 담는다.
        /*jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                estdList = new ArrayList<EnrolledStudentVO>();
                estdList.removeAll(esList);

                System.out.println("전부 출력" + estdList);

                SqlSession ss = factory.openSession();
                ss.insert("lec_std_t.deleteEnrolledStudent", esVO);
                ss.commit();
                ss.close();

                parent.enrolledStudentTable(estdList);
                dispose();
            }
        });*/


        //취소 버튼 클릭 시 닫기
        cancel_bt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });


        this.setTitle("수강생 추가");
        this.setBounds(640, 200, 520, 700);
        this.setVisible(true);
    }//생성자 끝

    //수강중인 학생 목록을 테이블에 추출하는 함수
    public void enrolledStudentTable(List<EnrolledStudentVO> esList){

        tableData = new String[esList.size()][c_name.length];
        int i = 0;
        for(EnrolledStudentVO esVO : esList){
            tableData[i][0] = esVO.getStdno(); //수강생 이름 출력
            tableData[i][1] = esVO.getStd_name(); //강의명
            tableData[i][2] = esVO.getStd_phone(); //수강생 연락처

            i++;
        }

        // 학생 테이블 비활성화
        jTable2.setModel(new DefaultTableModel(tableData, c_name){
            @Override
            public boolean isCellEditable(int row, int column) {

                return false;
            }
        });
        jTable2.revalidate();
        jTable2.updateUI();
    }

    //수강이 가능한 학생 목록 추출
    private void aVailableStd(ClassVO cVO){
        SqlSession ss = factory.openSession();
        stdList = ss.selectList("student.availableStudent", cVO);

        selectAvailableStdTable(stdList);
        System.out.println("찍어봐::::::::" + stdList.toString());
        ss.close();
    }

    //수강이 가능한 학생 리스트를 테이블에 표현
    private void selectAvailableStdTable(List<StudentVO> stdList){
        System.out.println("미미 " + stdList.toString());
        tableData = new String[stdList.size()][c_name.length];
        int i =0;
        for(StudentVO stVO : stdList){
            tableData[i][0] = stVO.getStdno(); //수강생 학번
            tableData[i][1] = stVO.getStd_name(); //수강생 이름
            tableData[i][2] = stVO.getStd_phone(); //수강생 전화번호
            i++;
        }
        //테이블 비활성화
        jTable1.setModel(new DefaultTableModel(tableData, c_name){
            @Override
            public boolean isCellEditable(int row, int column) {

                return false;
            }
        });

    }

    //db연결
    private void init(){
        try{
            Reader r = Resources.getResourceAsReader("pm/config/conf.xml");
            factory = new SqlSessionFactoryBuilder().build(r);
            r.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new JPanel();
        jPanel2 = new JPanel();
        jLabel1 = new JLabel();
        jPanel3 = new JPanel();
        jButton1 = new JButton();
        cancel_bt = new JButton();
        jPanel4 = new JPanel();
        jPanel5 = new JPanel();
        jLabel2 = new JLabel();
        jScrollPane1 = new JScrollPane();
        jTable1 = new JTable();
        jButton3 = new JButton();
        jPanel6 = new JPanel();
        jLabel3 = new JLabel();
        jScrollPane2 = new JScrollPane();
        jTable2 = new JTable();
        jButton4 = new JButton();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jLabel1.setFont(new java.awt.Font("맑은 고딕", 0, 18)); // NOI18N
        jLabel1.setText("수강생 추가");

        GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(194, 194, 194)
                                .addComponent(jLabel1)
                                .addContainerGap(207, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(53, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jButton1.setText("확인");

        cancel_bt.setText("취소");

        GroupLayout jPanel3Layout = new GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addContainerGap(301, Short.MAX_VALUE)
                                .addComponent(jButton1)
                                .addGap(18, 18, 18)
                                .addComponent(cancel_bt)
                                .addGap(34, 34, 34))
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addContainerGap(54, Short.MAX_VALUE)
                                .addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton1)
                                        .addComponent(cancel_bt))
                                .addGap(23, 23, 23))
        );

        jPanel1.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        jPanel4.setLayout(new java.awt.GridLayout(2, 2));

        jLabel2.setFont(new java.awt.Font("맑은 고딕", 0, 14)); // NOI18N
        jLabel2.setText("수강 가능한 학생");

        jTable1.setModel(new DefaultTableModel(tableData, c_name));
        jScrollPane1.setViewportView(jTable1);

        jButton3.setText("추가");

        GroupLayout jPanel5Layout = new GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
                jPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addGroup(jPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                .addComponent(jLabel2)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jButton3))
                                        .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 444, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(32, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
                jPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addGroup(jPanel5Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(jButton3))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(28, Short.MAX_VALUE))
        );

        jPanel4.add(jPanel5);


        jLabel3.setFont(new java.awt.Font("맑은 고딕", 0, 14)); // NOI18N
        jLabel3.setText("해당 강의를 수강중인 학생");

        jTable2.setModel(new DefaultTableModel(tableData, c_name));
        jScrollPane2.setViewportView(jTable2);

        jButton4.setText("제외");

        GroupLayout jPanel6Layout = new GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
                jPanel6Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addGroup(jPanel6Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(jScrollPane2, GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
                                        .addGroup(jPanel6Layout.createSequentialGroup()
                                                .addComponent(jLabel3)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jButton4)))
                                .addGap(34, 34, 34))
        );
        jPanel6Layout.setVerticalGroup(
                jPanel6Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addGroup(jPanel6Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3)
                                        .addComponent(jButton4))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 166, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.add(jPanel6);

        jPanel1.add(jPanel4, java.awt.BorderLayout.CENTER);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, 649, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton jButton1;
    private JButton cancel_bt;
    private JButton jButton3;
    private JButton jButton4;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JPanel jPanel3;
    private JPanel jPanel4;
    private JPanel jPanel5;
    private JPanel jPanel6;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JTable jTable1;
    private JTable jTable2;
    // End of variables declaration//GEN-END:variables
}
