import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class CalculMoyenne {
    static class Matiere {
        String nom;
        boolean aTP;
        double noteTP;
        double noteDS;
        double noteExamen;
        Matiere(String nom, double noteTP, double noteDS, double noteExamen) {
            this.nom       = nom;
            this.aTP       = true;
            this.noteTP    = noteTP;
            this.noteDS    = noteDS;
            this.noteExamen = noteExamen;
        }
        Matiere(String nom, double noteDS, double noteExamen) {
            this.nom       = nom;
            this.aTP       = false;
            this.noteDS    = noteDS;
            this.noteExamen = noteExamen;
        }

        double calculerMoyenne() {
            if (aTP) {
                return 0.15 * noteTP + 0.15 * noteDS + 0.70 * noteExamen;
            } else {
                return 0.30 * noteDS + 0.70 * noteExamen;
            }
        }
        @Override
        public String toString() {
            String details;
            if (aTP) {
                details = String.format("TP=%.2f (15%%)  DS=%.2f (15%%)  Examen=%.2f (70%%)",
                        noteTP, noteDS, noteExamen);
            } else {
                details = String.format("DS=%.2f (30%%)  Examen=%.2f (70%%)",
                        noteDS, noteExamen);
            }
            return String.format("%-20s | %s  →  Moyenne = %.2f/20",
                    nom, details, calculerMoyenne());
        }
    }
    static double lireNote(Scanner sc, String label) {
        double note;
        while (true) {
            System.out.print("    " + label + " (0-20) : ");
            if (sc.hasNextDouble()) {
                note = sc.nextDouble();
                if (note >= 0 && note <= 20) return note;
                System.out.println("    ⚠  La note doit être entre 0 et 20.");
            } else {
                System.out.println("    ⚠  Veuillez entrer un nombre valide.");
                sc.next();
            }
        }
    }
    static List<Matiere> saisirMatieres(Scanner sc) {
        List<Matiere> matieres = new ArrayList<>();

        System.out.print("\nNombre de matières : ");
        int n = 0;
        while (!sc.hasNextInt() || (n = sc.nextInt()) <= 0) {
            System.out.print("Veuillez entrer un entier positif : ");
            sc.next();
        }
        for (int i = 1; i <= n; i++) {
            System.out.println("\n── Matière " + i + " ──────────────────────────");
            System.out.print("  Nom de la matière : ");
            String nom = sc.next();

            System.out.print("  Cette matière a-t-elle un TP ? (o/n) : ");
            String rep = sc.next().toLowerCase();
            boolean aTP = rep.equals("o") || rep.equals("oui") || rep.equals("y") || rep.equals("yes");

            if (aTP) {
                double tp  = lireNote(sc, "Note TP");
                double ds  = lireNote(sc, "Note DS");
                double ex  = lireNote(sc, "Note Examen");
                matieres.add(new Matiere(nom, tp, ds, ex));
            } else {
                double ds  = lireNote(sc, "Note DS");
                double ex  = lireNote(sc, "Note Examen");
                matieres.add(new Matiere(nom, ds, ex));
            }
        }
        return matieres;
    }
    static void afficherResultats(List<Matiere> matieres) {
        System.out.println("\n╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║                    RÉCAPITULATIF DES NOTES                      ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝");
        System.out.println();

        double somme = 0;
        for (Matiere m : matieres) {
            System.out.println("  " + m);
            somme += m.calculerMoyenne();
        }
        double moyenneGenerale = somme / matieres.size();
        System.out.println();
        System.out.println("──────────────────────────────────────────────────────────────────");
        System.out.printf("  %-42s %.2f / 20%n", "Moyenne générale :", moyenneGenerale);
        System.out.println("──────────────────────────────────────────────────────────────────");
        String mention;
        if      (moyenneGenerale >= 16) mention = "✦ Très Bien";
        else if (moyenneGenerale >= 14) mention = "✦ Bien";
        else if (moyenneGenerale >= 12) mention = "✦ Assez Bien";
        else if (moyenneGenerale >= 10) mention = "✦ Passable";
        else                            mention = "✦ Insuffisant";

        System.out.printf("  Mention : %s%n%n", mention);
    }

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║         CALCULATEUR DE MOYENNE – MÊMES COEFFICIENTS             ║");
        System.out.println("║  • Avec TP  : TP 15% | DS 15% | Examen 70%                      ║");
        System.out.println("║  • Sans TP  :          DS 30% | Examen 70%                      ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝");

        Scanner sc = new Scanner(System.in);
        List<Matiere> matieres = saisirMatieres(sc);
        afficherResultats(matieres);
        sc.close();
    }
}