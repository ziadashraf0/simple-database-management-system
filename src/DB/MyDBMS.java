package DB;

import java.sql.SQLException;
import java.util.Scanner;

/**
 * Created by wagoo on 4/25/2017.
 */
public class MyDBMS implements Database {
    private Context context = new Context();

    @Override
    public boolean executeStructureQuery(String query) throws SQLException {

        String temp = query.toLowerCase();
        if (temp.startsWith("create")) {
            CreateState createState = new CreateState();
            context.setState(createState);
            if(context.doAction(query)) return true;

            return false;

        } else {
            DropState dropState = new DropState();
            context.setState(dropState);
            if(context.doAction(query)) return true;
            return false;

        }


    }

    @Override
    public Object[][] executeRetrievalQuery(String query) throws SQLException {
        SelectState selectState = new SelectState();
        context.setState(selectState);
        if (!selectState.doAction(query)) {
            throw new SQLException("syntax error");
        }
        else {
            return selectState.doQuery();
        }


    }

    @Override
    public int executeUpdateQuery(String query) throws SQLException {


        String temp = query.toLowerCase();
        if (temp.startsWith("insert")) {
            InsertState insertState = new InsertState();
            context.setState(insertState);
            if (context.doAction(query)) return 1;
            else
                return 0;
        } else {
            DeleteState deleteState = new DeleteState();
            context.setState(deleteState);
            if (context.doAction(query)) return 1;

                return 0;

        }
    }


    public static  void main(String[] args) throws SQLException {
        MyDBMS myDBMS = new MyDBMS();
        Scanner scanner = new Scanner(System.in);
        String s;
        System.out.println("Please enter regex to do operation from the following or press 6 to exit");
        System.out.println("1-create table");
        System.out.println("2-drop table");
        System.out.println("3-insert record");
        System.out.println("4-delete record");
        System.out.println("5-select table");
        System.out.println("6-exit");


        try {
            for (int i = 0; i < 10; i++) {
                s = scanner.nextLine();
                String temp = s.toLowerCase();
                if (s.startsWith("create")) {
                    myDBMS.executeStructureQuery(s);
                } else if (s.startsWith("drop")) {
                    myDBMS.executeStructureQuery(s);
                } else if (s.startsWith("insert")) {
                    myDBMS.executeUpdateQuery(s);
                } else if (s.startsWith("delete")) {
                    myDBMS.executeUpdateQuery(s);
                } else if (s.startsWith("select")) {
                    myDBMS.executeRetrievalQuery(s);
                } else if (s.charAt(0) == '6') {
                    System.out.println("See you later <3");
                    break;
                }
            }



        /*try{
            // myDBMS.executeStructureQuery("drop table ziad;");
             myDBMS.executeStructureQuery("Create table O (firstName varchar,middleName int,lastName varchar,age int);");
          // myDBMS.executeUpdateQuery("insert into O (firstName,middleName,lastName,age) Values(Ahmed,2,55,55)");
            myDBMS.executeUpdateQuery("insert into O (firstName,middleName,lastName,age) Values(gaega,55,gaeg,55)");
          // myDBMS.executeUpdateQuery("insert into O (lastName) Values(omar)");
            //myDBMS.executeUpdateQuery("delete from O where firstName=alyg");
          // myDBMS.executeRetrievalQuery("SELECT firstName, lastName, FROM O WHERE firstName=Ahmed;");
            //myDBMS.executeStructureQuery("drop table O");*/
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }}

