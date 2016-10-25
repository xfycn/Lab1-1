package lab;
//findbugs֮��Ĵ���
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

public class EvaExpression {

  public static void main(String[] args) {
    String nstr = "";
    System.out.println("����'Exit'�˳�����");
    Scanner n = new Scanner(System.in);
    ProExpression exp = new ProExpression();
    while (true) {
      nstr = n.nextLine();// ����
      if ("Exit".equals(nstr)) {
        break;
      }
      long starTime = System.currentTimeMillis();// ���Գ�������ʱ��
      exp.ProExp(nstr);
      System.out.println("����ʼʱ�䣺" + starTime + "����");//
      long endTime = System.currentTimeMillis();//
      System.out.println("�������ʱ�䣺" + endTime + "����");//
      long Time = endTime - starTime;//
      System.out.println("����ִ��ʱ�䣺" + Time + "����");//
    }
    n.close();
  }

}

class ProExpression {
  String memStr = ""; // �洢�Ϸ��ı��ʽ
  String derstr = ""; // �洢�󵼵Ľ��
  String estr = ""; // �жϱ��ʽ����Ľ��

  // ������ʽ
  private String StaExp(String str) {
    String sestr;
    sestr = str;
    int j = 0;
    while (j != -1) {// ��x^2ת��Ϊx*x��
      j = sestr.indexOf('^');
      if (j != -1) {
        String str0 = sestr.substring(j - 1, j);
        String str1 = "";
        int i;
        for (i = j + 1; i < sestr.length(); i++) {
          if (!(sestr.charAt(i) >= 48 && sestr.charAt(i) <= 57)) {
            break;
          }
        }
        String str2 = "";
        str2 = sestr.substring(j + 1, i);
        int h = Integer.valueOf(str2).intValue();
        for (int k = 0; k < h; k++) {
          str1 = str1 + str0 + "*";
        }
        sestr = sestr.substring(0, j - 1) + str1.substring(0, str1.length() - 1) + sestr.substring(i);
      }
    }
    String rstr = "";// ��ϵ���������������������
    String[] sestrArray = sestr.split("\\+");
    String[][] Array = new String[sestrArray.length][2];
    for (int k = 0; k < sestrArray.length; k++) {
      String[] _sestrArray = sestrArray[k].split("\\*");
      float num = 1;
      String str0 = "";
      String[] str1 = new String[_sestrArray.length];
      int m = 0;
      for (int l = 0; l < _sestrArray.length; l++) {
        char ch = _sestrArray[l].charAt(0);
        if ((ch >= '0' && ch <= '9') || ch == '-') {
          num = num * Float.valueOf(_sestrArray[l]).floatValue();
        } else {
          str1[m++] = _sestrArray[l];
        }
      }
      Array[k][0] = String.valueOf(num);
      if (m > 0) {
        String[] str2 = new String[m];
        for (int n = 0; n < m; n++) {
          str2[n] = str1[n];
        }
        Arrays.sort(str2);
        for (int n = 0; n < m; n++) {
          str0 += str2[n] + "*";
        }
        Array[k][1] = str0.substring(0, str0.length() - 1);
      } else {
        Array[k][1] = "";
      }
    }
    for (int k = 0; k < sestrArray.length - 1; k++) {
      for (int l = k + 1; l < sestrArray.length; l++) {
        if (Array[k][1].equals(Array[l][1])) {
          Array[k][0] = String
              .valueOf(Float.valueOf(Array[k][0]).floatValue() + Float.valueOf(Array[l][0]).floatValue());
          Array[l][1] = "#";
        }
      }
    }
    for (int k = 0; k < sestrArray.length; k++) {
      if (Array[k][1] != "#") {
        if (Array[k][1] != "") {
          rstr += Array[k][0] + "*" + Array[k][1] + "+";
        } else {
          rstr += Array[k][0] + "+";
        }
      }
    }
    return rstr.substring(0, rstr.length() - 1);
  }

  // �жϱ��ʽ�Ƿ���ȷ
  private boolean expression(String str) {
    str = str.replace(" ", "");
    str = str.replace(" ", "");
    boolean result;
    int flag = 0;
    int i = 0;
    char ch0, ch1, ch2;
    if (str.length() == 0) {
      flag = 1;
    } else if (str.length() == 1) {// ���ַ�
      char ch = str.charAt(0);
      if (!((ch >= 48 && ch <= 57) || (ch >= 65 && ch <= 90) || (ch >= 97 && ch <= 122))) {
        flag = 1;
      }
    } else {
      for (i = 0; i < str.length() - 1; i++) {
        ch0 = str.charAt(i);
        ch1 = str.charAt(i + 1);
        if (ch0 == 42 || ch0 == 43) {// *+
          if (i == 0 || (!((ch1 >= 48 && ch1 <= 57) || (ch1 >= 65 && ch1 <= 90) || (ch1 >= 97 && ch1 <= 122)))) {
            flag = 1;
            break;
          }
        } else if (ch0 == 45) {// -
          if (i == 0 || (!((ch1 >= 48 && ch1 <= 57) || (ch1 >= 65 && ch1 <= 90) || (ch1 >= 97 && ch1 <= 122)))) {
            flag = 1;
            break;
          } else {
            str = str.substring(0, i) + "+" + str.substring(i);
            i += 1;
          }
        } else if (ch0 == 94) {// ^
          if (i == 0) {
            flag = 1;
            break;
          } else {
            char ch_1 = str.charAt(i - 1);
            if (!(((ch_1 >= 65 && ch_1 <= 90) || (ch_1 >= 97 && ch_1 <= 122) || (ch_1 >= 48 && ch_1 <= 57))
                && (ch1 >= 48 && ch1 <= 57))) {
              flag = 1;
              break;
            }
          }
        } else if (ch0 >= 48 && ch0 <= 57) {// 3x��300��3+/*/-/^
          if ((ch1 >= 48 && ch1 <= 57) || (ch1 == 42 || ch1 == 43 || ch1 == 45 || ch1 == 94)) {
            str = str.substring(0, i + 1) + "" + str.substring(i + 1);
          } else if ((ch1 >= 65 && ch1 <= 90) || (ch1 >= 97 && ch1 <= 122)) {
            str = str.substring(0, i + 1) + "*" + str.substring(i + 1);
          } else {
            flag = 1;
            break;
          }
        } else if ((ch0 >= 65 && ch0 <= 90) || (ch0 >= 97 && ch0 <= 122)) {// x3
                                                                           // xy
          if ((ch1 >= 48 && ch1 <= 57) || (ch1 >= 65 && ch1 <= 90) || (ch1 >= 97 && ch1 <= 122)) {
            str = str.substring(0, i + 1) + "*" + str.substring(i + 1);
          } else if (ch1 == 42 || ch1 == 43 || ch1 == 45 || ch1 == 94) {
            str = str.substring(0, i + 1) + "" + str.substring(i + 1);
          } else {
            flag = 1;
            break;
          }
        } else {
          flag = 1;
          break;
        }
      }
      ch2 = str.charAt(str.length() - 1);
      if (ch2 == 42 || ch2 == 43 || ch2 == 45 || ch2 == 94) {
        flag = 1;
      }
    }
    if (flag == 1) {
      estr = "Expressions is not valid!";
      result = false;
    } else {
      estr = str;
      estr = StaExp(estr);
      result = true;
    }
    return result;
  }

  // ����ֵ�滻��Ӧ�ı���
  private String simplify(String str) {
    int i;
    int flag = 0;
    String sstr = memStr;
    String[] strArray = str.split(" ");
    if (strArray.length > 1) {
      for (i = 1; i < strArray.length; i++) {
        String[] _strArray = strArray[i].split("=");
        int k = sstr.indexOf(_strArray[0]);
        if (k != -1) {
          sstr = sstr.replace(_strArray[0], _strArray[1]);
        } else {
          System.out.println("Variable Error!");
          flag = 1;
          break;
        }
      }
    }
    if (flag == 1) {
      return "";
    } else {
      return StaExp(sstr);
    }
  }

  // ��
  private boolean derivative(String str) {
    derstr = "";
    int i;
    int cnt = 0;
    String[] strArray = str.split(" ");
    String[] cstrArray = memStr.split("\\+");
    for (i = 0; i < cstrArray.length; i++) {
      String estr = "";
      int k = cstrArray[i].indexOf(strArray[1]);
      if (k != -1) {
        int cnt1 = 0;
        for (int j = 0; j < cstrArray[i].length(); j++) {// �������������ȷ��ϵ��
          if (cstrArray[i].charAt(j) == strArray[1].charAt(0)) {
            cnt1 += 1;
          }
        }
        estr = cstrArray[i].substring(0, k) + cstrArray[i].substring(k + 1);
        if (cstrArray[i].charAt(k) == '*') {
          estr = estr.substring(0, k) + estr.substring(k + 1);
        } else if (cstrArray[i].charAt(k - 1) == '*') {
          estr = estr.substring(0, k - 1) + estr.substring(k);
        }
        if (cnt1 > 1) {
          estr = estr + "*" + String.valueOf(cnt1);
        }
        derstr += estr;
        derstr += "+";
      } else {
        cnt += 1;
      }
    }
    if (cnt == cstrArray.length) {
      System.out.println("Variable Error!");
      return false;
    } else {
      derstr = derstr.substring(0, derstr.length() - 1);
      derstr = StaExp(derstr);
      return true;
    }
  }

  // �ж������� ������/������/���ʽ��
  public void ProExp(String str) {
    boolean result1 = Pattern.matches("!simplify( [a-z | A-Z]=-{0,1}[0-9]{0,}\\.{0,1}[0-9]*)*", str);
    boolean result2 = Pattern.matches("!d/d [a-z | A-Z]", str);
    if (result1 == true) {
      String str0 = simplify(str);
      if (!str0.equals("")) {
        System.out.println(str0);
      }
    } else if (result2 == true) {
      if (derivative(str)) {
        System.out.println(derstr);
      }
    } else {
      boolean r = expression(str);
      if (r == true) {
        this.memStr = this.estr;
        System.out.println(memStr);
      } else {
        System.out.println(estr);
      }
    }
  }
}
