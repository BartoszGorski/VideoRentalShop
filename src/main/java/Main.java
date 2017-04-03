
public class Main {

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName());
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                System.out.println(Thread.currentThread().getName());
                AppWindow app = new AppWindow();
                app.createAndShowGUI();
            }
        });
    }
}
