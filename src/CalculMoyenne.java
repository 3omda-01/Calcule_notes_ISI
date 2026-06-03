import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class CalculMoyenne {
    enum TypeMatiere {
        AVEC_TP,
        SANS_TP,
        UNIVERSELLE
    }
    static class Matiere {
        String nom;
        TypeMatiere type;
        double coefficient;
        double noteTP;
        double noteDS;
        double noteCC;
        double noteExamen;
        Matiere(String nom, double noteTP, double noteDS, double noteExamen) {
            this.nom        = nom;
            this.type       = TypeMatiere.AVEC_TP;
            this.coefficient = 1.5;
            this.noteTP     = noteTP;
            this.noteDS     = noteDS;
            this.noteExamen = noteExamen;
        }
        Matiere(String nom, double noteDS, double noteExamen) {
            this.nom        = nom;
            this.type       = TypeMatiere.SANS_TP;
            this.coefficient = 1.5;
            this.noteDS     = noteDS;
            this.noteExamen = noteExamen;
        }
        Matiere(String nom, double noteCC, double noteDS, boolean universelle) {
            this.nom    = nom;
            this.type   = TypeMatiere.UNIVERSELLE;
            this.coefficient = 1;
            this.noteCC = noteCC;
            this.noteDS = noteDS;
        }

        double calculerMoyenne() {
            switch (type) {
                case AVEC_TP:    return 0.15 * noteTP + 0.15 * noteDS + 0.70 * noteExamen;
                case SANS_TP:    return 0.30 * noteDS + 0.70 * noteExamen;
                case UNIVERSELLE:return 0.80 * noteCC + 0.20 * noteDS;
                default:         return 0;
            }
        }
        @Override
        public String toString() {
            String details;
            switch (type) {
                case AVEC_TP:
                    details = String.format("TP=%.2f (15%%)  DS=%.2f (15%%)  Examen=%.2f (70%%)",
                            noteTP, noteDS, noteExamen);
                    break;
                case SANS_TP:
                    details = String.format("DS=%.2f (30%%)  Examen=%.2f (70%%)",
                            noteDS, noteExamen);
                    break;
                case UNIVERSELLE:
                    details = String.format("CC=%.2f (80%%)  DS=%.2f (20%%)",
                            noteCC, noteDS);
                    break;
                default:
                    details = "";
            }
            return String.format("%-20s | Coeff=%.1f | %s  ->  Moyenne = %.2f/20",
                    nom, coefficient, details, calculerMoyenne());
        }
    }
    static double lireNote(Scanner sc, String label) {
        double note;
        while (true) {
            System.out.print("    " + label + " (0-20) : ");
            if (sc.hasNextDouble()) {
                note = sc.nextDouble();
                if (note >= 0 && note <= 20) return note;
                System.out.println("    !  La note doit etre entre 0 et 20.");
            } else {
                System.out.println("    !  Veuillez entrer un nombre valide.");
                sc.next();
            }
        }
    }
    static List<Matiere> saisirMatieres(Scanner sc) {
        List<Matiere> matieres = new ArrayList<>();
        System.out.println("\n=== SAISIE DES 11 MATIERES ===");
        System.out.println("  8 premieres : matieres normales (coeff 1.5)");
        System.out.println("  3 dernieres : universelles (coeff 1)");
        for (int i = 1; i <= 11; i++) {
            System.out.println("\n-- Matiere " + i + "/11 -------------------------");
            System.out.print("  Nom de la matiere : ");
            String nom = sc.next();
            if (i <= 8) {
                System.out.println("  Type de matiere (coeff 1.5) :");
                System.out.println("    [1] Normale avec TP  -> TP 15% + DS 15% + Examen 70%");
                System.out.println("    [2] Normale sans TP  -> DS 30% + Examen 70%");
                System.out.print("  Votre choix (1/2) : ");
                int choix = 0;
                while (choix < 1 || choix > 2) {
                    if (sc.hasNextInt()) {
                        choix = sc.nextInt();
                        if (choix < 1 || choix > 2)
                            System.out.print("  Choix invalide, entrez 1 ou 2 : ");
                    } else {
                        System.out.print("  Choix invalide, entrez 1 ou 2 : ");
                        sc.next();
                    }
                }
                if (choix == 1) {
                    double tp  = lireNote(sc, "Note TP");
                    double ds1 = lireNote(sc, "Note DS");
                    double ex1 = lireNote(sc, "Note Examen");
                    matieres.add(new Matiere(nom, tp, ds1, ex1));
                } else {
                    double ds2 = lireNote(sc, "Note DS");
                    double ex2 = lireNote(sc, "Note Examen");
                    matieres.add(new Matiere(nom, ds2, ex2));
                }
            } else {
                System.out.println("  Matiere universelle (coeff 1) -> CC 80% + DS 20%");
                double cc = lireNote(sc, "Note CC");
                double ds = lireNote(sc, "Note DS");
                matieres.add(new Matiere(nom, cc, ds, true));
            }
        }
        return matieres;
    }
    static void afficherResultats(List<Matiere> matieres) {
        System.out.println("\n+==================================================================+");
        System.out.println("|                   RECAPITULATIF DES NOTES                       |");
        System.out.println("+==================================================================+");
        System.out.println();
        double sommePonderee = 0;
        double sommeCoeffs = 0;
        for (Matiere m : matieres) {
            System.out.println("  " + m);
            sommePonderee += m.calculerMoyenne() * m.coefficient;
            sommeCoeffs += m.coefficient;
        }
        double moyenneGenerale = sommePonderee / sommeCoeffs;
        System.out.println();
        System.out.println("------------------------------------------------------------------");
        System.out.printf("  %-42s %.2f / 20%n", "Moyenne generale (ponderee) :", moyenneGenerale);
        System.out.printf("  %-42s %.1f%n", "Total des coefficients :", sommeCoeffs);
        System.out.println("------------------------------------------------------------------");
        String mention;
        if      (moyenneGenerale >= 16) mention = "Tres Bien";
        else if (moyenneGenerale >= 14) mention = "Bien";
        else if (moyenneGenerale >= 12) mention = "Assez Bien";
        else if (moyenneGenerale >= 10) mention = "Passable";
        else                            mention = "Insuffisant";

        System.out.printf("  Mention : %s%n%n", mention);
    }
    public static void main(String[] args) {
        System.out.println("+==================================================================+");
        System.out.println("|        CALCULATEUR DE MOYENNE - 11 MATIERES                     |");
        System.out.println("|  8 matieres coeff 1.5 : [1] Avec TP ou [2] Sans TP              |");
        System.out.println("|  3 matieres coeff 1   : [3] Universelle (CC 80% + DS 20%)       |");
        System.out.println("|  Moyenne ponderee sur 20                                        |");
        System.out.println("+==================================================================+");

        Scanner sc = new Scanner(System.in);
        List<Matiere> matieres = saisirMatieres(sc);
        afficherResultats(matieres);
        sc.close();
    }
}