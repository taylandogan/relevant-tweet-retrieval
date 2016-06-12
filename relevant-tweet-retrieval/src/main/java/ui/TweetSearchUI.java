package ui;

import ir.LuceneIndexSearch;
import ir.LuceneIndexer;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.ComponentOrientation;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import twitter.Tweet;
import db.DbOperations;

@SuppressWarnings("serial")
public class TweetSearchUI extends JFrame {

	public TweetSearchUI() {
		initUI();
	}
	
	
	public void initUI() {
		JPanel mainPanel = new JPanel();
	    getContentPane().add(mainPanel);
	    mainPanel.setLayout(new CardLayout());
	    
	    // Create text field
	    JPanel textPanel = new JPanel();
	    JTextField textField = new JTextField();
	    textField.setColumns(30);
	    textField.setBounds(50, 60, 200, 30);
	    textPanel.add(textField);
	    
	    // Create result panel
	    JPanel resultPanel = new JPanel();
	    //String[] columnNames = {"Id", "Clean", "Raw", "Username", "Creation Date", "RT", "FAV"};
	    String[] columnNames = {"Id", "Username", "Raw Tweet"};
	    DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
	    JTable table = new JTable(tableModel);
	    table.setMaximumSize(new Dimension(600, 400));
	    table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
	    
	    // When a row is selected:
	    table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent event) {
				if(table.getSelectedRow() > -1) {
					JDialog dialog = new JDialog();
					dialog.setTitle("Selected Tweet");
					dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
					dialog.setLayout(new FlowLayout());
					dialog.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
					dialog.setBounds(350, 350, 200, 200);
					
					//List<JLabel> tweetInfoLabels = new ArrayList<>();
					
					for(int i =0; i < table.getColumnCount(); i++) {
						JLabel label = new JLabel(table.getValueAt(table.getSelectedRow(), i).toString() + "\n");
						dialog.add(label);
					}
					
					dialog.setVisible(true);
				}
			}
		});
	    
	    JScrollPane scrollable = new JScrollPane(table);
	    scrollable.setPreferredSize(new Dimension(600, 400));
	    scrollable.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
	    scrollable.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    resultPanel.add(scrollable);
	    
	    // Create search button
	    JPanel searchButtonPanel = new JPanel();
	    JButton searchButton = new JButton("Search");
	    searchButton.setBounds(50, 60, 200, 300);
	    searchButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent event) {
	    		String enteredText = textField.getText();
	    		List<Tweet> resultingTweets = queryEnteredInput(enteredText);
	    		
	    		// Append resulting tweets to table
	    		for (int i = 0; i < resultingTweets.size(); i++) {
		    		List<String> tweetString = new ArrayList<String>();
		    		tweetString.add(String.valueOf(resultingTweets.get(i).tweetId));
		    		tweetString.add(resultingTweets.get(i).username);
		    		tweetString.add(resultingTweets.get(i).rawText);
		    		tableModel.addRow(tweetString.toArray());
	    		}
	    		
	    		table.setFillsViewportHeight(true);
	    	}
	    });
	    searchButtonPanel.add(searchButton);
	   
	    // Add widgets to panel
	    add(textPanel, BorderLayout.NORTH);
	    add(searchButtonPanel);
	    add(resultPanel, BorderLayout.SOUTH);
	    //mainPanel.add(textPanel);
	    //mainPanel.add(searchButtonPanel);

	    setTitle("Relevant Tweet Retrieval");
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
	
	public List<Tweet> queryEnteredInput(String queryText) {
		long b = System.currentTimeMillis();
		LuceneIndexSearch lis = new LuceneIndexSearch();
		List<Long> idList = lis.searchIndex(queryText, "clean");
		long c = System.currentTimeMillis();
		
		List<Tweet> tweetList = null;
		try {
			tweetList = DbOperations.getTweetsByIdFromDB(idList);
		} catch (SQLException e) {
			System.out.println("Error while fetching tweets from DB: " + e.getMessage());
		}
		long d = System.currentTimeMillis();

		System.out.println();
		System.out.println("Searching the index took: " + (c-b) + " ms.");
		System.out.println("Fetching tweets took: " + (d-c) + " ms.");
		
		return tweetList;
	}

    public static void main(String[] args) throws IOException {
    	System.out.println("Building Lucene index..");
    	long a = System.currentTimeMillis();
		LuceneIndexer li = new LuceneIndexer();
		li.createIndex();
		long b = System.currentTimeMillis();
		System.out.println("Building the index took: " + (b-a) + " ms.");
		System.out.println("Starting GUI now..");

        EventQueue.invokeLater(new Runnable() {
        
            @Override
            public void run() {
                TweetSearchUI ui = new TweetSearchUI();
                ui.setVisible(true);
            }
        });
    }
}
