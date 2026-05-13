import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class CrazyWheel extends JFrame {
private double saldo = 1000.0;
private String[] spicchi = {"1", "2", "5", "10", "BONUS", "1", "2", "5", "10", "CRAZY"};
private int[] moltiplicatori = {1, 2, 5, 10, 20, 1, 2, 5, 10, 50};

private JLabel lblSaldo, lblRisultato;
private JTextField txtPuntata;
private JComboBox<String> comboScommessa;
private JButton btnGira;

public CrazyWheel() {
setTitle("Java Crazy Wheel Simulator");
setSize(400, 300);
setDefaultCloseOperation(EXIT_ON_CLOSE);
setLayout(new GridLayout(6, 1, 10, 10));

lblSaldo = new JLabel("Saldo: €" + saldo, SwingConstants.CENTER);
lblRisultato = new JLabel("Tenta la fortuna!", SwingConstants.CENTER);
txtPuntata = new JTextField("10");
comboScommessa = new JComboBox<>(new String[]{"1", "2", "5", "10", "BONUS", "CRAZY"});
btnGira = new JButton("GIRA LA RUOTA!");

add(lblSaldo);
add(new JLabel("Importo da puntare:", SwingConstants.CENTER));
add(txtPuntata);
add(new JLabel("Scommetti su:", SwingConstants.CENTER));
add(comboScommessa);
add(btnGira);

btnGira.addActionListener(e -> avviaGioco());
}

private void avviaGioco() {
try {
double puntata = Double.parseDouble(txtPuntata.getText());
String scommessa = (String) comboScommessa.getSelectedItem();

if (puntata > saldo || puntata <= 0) {
JOptionPane.showMessageDialog(this, "Saldo insufficiente o puntata errata!");
return;
}

btnGira.setEnabled(false);

// Simulazione rotazione (Semplice selezione random)
Random r = new Random();
int indiceEstratto = r.nextInt(spicchi.length);
String risultatoRuota = spicchi[indiceEstratto];
int molt = moltiplicatori[indiceEstratto];

double vincita = 0;
if (risultatoRuota.equals(scommessa)) {
vincita = puntata * molt;
saldo += vincita;
lblRisultato.setText("HAI VINTO! Uscito " + risultatoRuota + " (x" + molt + ")");
} else {
saldo -= puntata;
lblRisultato.setText("Perso! Uscito " + risultatoRuota);
}

lblSaldo.setText("Saldo: €" + saldo);
salvaSuCSV(scommessa, puntata, risultatoRuota, vincita);
btnGira.setEnabled(true);

} catch (NumberFormatException ex) {
JOptionPane.showMessageDialog(this, "Inserisci un numero valido!");
}
}

private void salvaSuCSV(String scommessa, double puntata, String uscito, double vincita) {
try (FileWriter writer = new FileWriter("giocate.csv", true)) {
writer.append(String.format("%s,%.2f,%s,%.2f,%.2f\n",
scommessa, puntata, uscito, vincita, saldo));
} catch (IOException e) {
System.out.println("Errore nel salvataggio CSV");
}
}

public static void main(String[] args) {
SwingUtilities.invokeLater(() -> new CrazyWheel().setVisible(true));
}
}