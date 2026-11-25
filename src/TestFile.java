// Программа приветствия
public class testdir {
    private String userName;

    public TestFile(String name) {
        this.userName = name;
    }

    public void sayHello() {
        int count = 3;
        for (int i = 0; i < count; i++) {
            System.out.println("Hello, " + userName);
        }
    }

    public static void main(String[] args) {
        /* создаём объект */
        testdir app = new testdir();
        app.sayHello();
    }
}
