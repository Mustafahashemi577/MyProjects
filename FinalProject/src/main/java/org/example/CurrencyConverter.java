package org.example;
import javax.swing.*;
import java.io.IOException;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class CurrencyConverter extends JFrame implements ActionListener {
    private String currency[] = {"AUD  Australia Dollar","GBP Great Britain Pound", "JPY Japan Yen", "EUR Euro",
            "USD United States Dollar", "AZN Azerbaijan New Manat", "AFN Afghani", "BHD Bahrain Dinar", "KHR Cambodia Rial ",
            "CAD Canada Dollar", "CLP Chili Peso", "CNY China Yuan-Renminbi", "COP Colombia Peso", "DKK Denmark Krone", "EGP Egypt Pound",
            "FIM Finland Markka", "INR Indian Rupees", "IDR Indonesia Rupiah", "IRR Iran Rial", "IED Ireland Pound", "IQD Iraq Dinar",
            "ISL Israel Shekel", "JOD Jordan Dinar", "KZT Kazakestan Tenge", "KWD Kuwait Dinar", "KGS Kyrgyzstan Som", "LBP Lebanon Pound",
            "MVR Maldives Rufiyaa", "MAD Marocco Dirham", "NZD New Zealand Dollar", "OMR Oman Rial", "PKR Pakistan Rupee", "QAR Qatar Rial",
            "RUB Russia Ruble", "SAR Saudi Arabia Riyal", "SGD Singapore Dollar", "LKR Sri Lanka Rupee", "TRY Turkish Lira", "UAH Ukraine hryvnia",
            "AED United Arab Emirates Dirham", "ZWD Zimbabwe Dollar"};
    String urlString ;
    URL url ;
    HttpURLConnection conn;
    JComboBox<String> boxFrom, boxTo;
    JMenuBar menuBar;
    JMenu fileMenu;
    JMenuItem history, about,exit;
    JButton convertButton;
    Map <String,Double> currencies= new HashMap<>();
    JTextField result, amount, perCurrency;
    GridBagConstraints gbcTop, gbcDown;
    String nameTo, nameFrom,basedOn;
    double enteredAmount,resultigValue;
    JPanel top, panel;

    CurrencyConverter() {
        setupTopFrame();
        setupMainFrame();
        addingListeners();
    }
    private void addingListeners(){
        CurrencyConverter parent=this;
        convertButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(nameTo==null||nameFrom==null){
                    JOptionPane.showMessageDialog(parent,"Please select the currencies you want to covert from and to!");
                }
                else{
                    try {
                        enteredAmount = Double.parseDouble(amount.getText());
                    }
                    catch (NumberFormatException ex){
                        JOptionPane.showMessageDialog(parent,"Invalid Number!!!");
                    }
                    try {
                        parent.convertButtonOperations();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }

            }
        });
    }
    private void convertButtonOperations() throws IOException {
        urlString="https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/currencies/"+basedOn+".json";
        url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        int responseCode = conn.getResponseCode();
        CurrencyConverter parent = this;
        if(responseCode!=200)
            JOptionPane.showMessageDialog(parent,"Bad Internet Connection, Cannot connect to API");
        else{
            Scanner scan = new Scanner(url.openStream());
            int counter = 1;
            while (scan.hasNext()){
                String temp[] = scan.nextLine().split(":");
                if(counter>=6 && counter<344) {
                    temp[1] = temp[1].substring(0, temp[1].length() - 1);
                    currencies.put(temp[0].substring(3, 6), Double.parseDouble(temp[1]));
                }
                counter++;
            }
            double temp = currencies.get(nameTo);
            String temp2 = String.format("%.5f",temp);
            perCurrency.setText(temp2+" "+nameTo.toUpperCase());
            resultigValue = enteredAmount * temp;
            String formatted = String.format("%.3f",resultigValue);
            result.setText(formatted+" "+nameTo.toUpperCase());
        }
    }
    private void setupMainFrame(){
        panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 240));
        gbcDown = new GridBagConstraints();
        gbcDown.insets = new Insets(10, 0, 0, 0);
        gbcDown.fill = GridBagConstraints.HORIZONTAL;
        gbcDown.gridy = 0;
        gbcDown.anchor = GridBagConstraints.NORTHWEST;
        add(panel, BorderLayout.CENTER);
        gbcDown.gridy++;
        // From Label
        panel.add(new JLabel("From ") {
            {
                setFont(new Font("Arial", Font.PLAIN, 18));
            }
        }, gbcDown);
        gbcDown.gridy++;
        Arrays.sort(currency);
        boxFrom = new JComboBox<>(currency);
        boxFrom.setPreferredSize(new Dimension(180, 35)); // Set preferred size
        panel.add(boxFrom, gbcDown);
        boxFrom.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nameFrom = (String) boxFrom.getSelectedItem();
                if(nameFrom!=null)
                    basedOn=nameFrom.substring(0,3).toLowerCase();
            }
        });
        gbcDown.gridy++;
        // To Label

        panel.add(new JLabel("To ") {
            {
                setFont(new Font("Arial", Font.PLAIN, 18));
            }
        }, gbcDown);
        gbcDown.gridy++;


        boxTo = new JComboBox<>(currency);
        boxTo.setPreferredSize(new Dimension(150, 35));
        panel.add(boxTo, gbcDown);
        boxTo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nameTo = (String) boxTo.getSelectedItem();
                if(nameTo!=null) {
                    nameTo = nameTo.substring(0, 3).toLowerCase();
                }
            }
        });
        gbcDown.gridy++;


        panel.add(new JLabel("Amount ") {
            {
                setFont(new Font("Arial", Font.PLAIN, 15));
            }
        }, gbcDown);
        gbcDown.gridy++;
        CurrencyConverter parent = this;
        amount = new JTextField();
        amount.setText("0");
        amount.setPreferredSize(new Dimension(150, 35));
        amount.setFont(new Font("Arial",Font.PLAIN,18));
        amount.setBackground(Color.decode("#e6e6e6"));
        panel.add(amount, gbcDown);
        gbcDown.gridy++;

        // Convert Button
        convertButton = new JButton("Convert");
        convertButton.setBackground(new Color(0, 200, 120));
        convertButton.setFont(new Font("Arial", Font.BOLD, 15));
        convertButton.setForeground(Color.white);
        convertButton.setFocusPainted(false);
        panel.add(convertButton, gbcDown);
        gbcDown.gridy++;

        panel.add(new JLabel("Per") {
            {
                setFont(new Font("Arial", Font.PLAIN, 15));
            }
        }, gbcDown);

        gbcDown.gridy++;

        perCurrency = new JTextField(15);
        perCurrency.setPreferredSize(new Dimension(150,35));
        perCurrency.setEditable(false);
        perCurrency.setBackground(Color.decode("#e6e6e6"));
        perCurrency.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        perCurrency.setFont(new Font("Arial",Font.BOLD,15));
        panel.add(perCurrency, gbcDown);
        gbcDown.gridy++;

        // Result Label
        panel.add(new JLabel("Result ") {
            {
                setFont(new Font("Arial", Font.PLAIN, 15));
            }
        }, gbcDown);

        gbcDown.gridy++;
        // Result Output Field
        result = new JTextField(15);
        result.setPreferredSize(new Dimension(150, 35));
        result.setEditable(false);
        result.setBackground(Color.decode("#e6e6e6"));
        result.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        result.setFont(new Font("Arial",Font.BOLD,15));
        panel.add(result, gbcDown);
    }


    private void setupTopFrame(){
        setTitle("CurrencyConverter");
        setSize(350,600);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        top =new JPanel();
        top.setLayout(new GridBagLayout());
        top.setBackground(new Color(245,245,240));
        gbcTop =new GridBagConstraints();
        gbcTop.insets =new Insets(10,0,0,77);
        gbcTop.fill =GridBagConstraints.HORIZONTAL;
        gbcTop.gridx =0;
        gbcTop.gridy =0;
        gbcTop.gridwidth =0;
        gbcTop.anchor =GridBagConstraints.EAST;
        top.add(new JLabel("Currency") {
        {
            setFont(new Font("Arial", Font.BOLD, 25));
            setForeground(new Color(0, 60, 200));
        }
        },gbcTop);
        gbcTop.gridy++;
        top.add(new JLabel("Converter") {
            {
                setFont(new Font("Arial", Font.BOLD, 25));
                setForeground(new Color(0, 60, 200));
            }
        }, gbcTop);
        add(top, BorderLayout.NORTH);
    }
    public static void main(String[] args) {
        new CurrencyConverter().setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}