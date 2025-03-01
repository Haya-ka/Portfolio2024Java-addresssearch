package sample04;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class AddressSearch extends JFrame {
	//ウィンドウ制御
	private final Image bgimage = new ImageIcon("BGimage.png").getImage();
	private final Image image = new ImageIcon("HOME.png").getImage();
	private final Image poskuma = new ImageIcon("poskuma.png").getImage();
	private JComboBox<String> searchMethodComboBox;				// 検索方法のコンボボックス
	private JPanel leftPanel;									// 左側の設定パネル
	private JPanel rightPanel;									// 右側の検索結果表示パネル
	private JTextField textField1, textField2, resultcntField;	// 入力フィールド
	private JTextField selectedFile;							// 選択中のファイル名表示
	private JButton searchButton;								// 検索ボタン
	JLabel dialog;
	JLabel word1 = new JLabel("▼ 郵便番号を入力");
	JLabel word2 = new JLabel("－");
	JLabel resultTitle = new JLabel("　<< 検索結果 >>");
	JLabel resultCall = new JLabel("表示件数：0");
	JLabel blank = new JLabel("　");
	// メニューバー
	JMenuBar menuBar = new JMenuBar();
	JMenu menuFile = new JMenu("ファイル");	
	JMenuItem menuOpen = new JMenuItem("検索ファイルを指定");
	JMenuItem menuExit = new JMenuItem("終了");
	//フィールド変数
	private int currentPage = 0;
	private int perpage = 20;
	private SearchSys ss;
	static ArrayList<Data> searchResults = new ArrayList<Data>();
	static String input1 = "";
	static String input2 = "";
	static Boolean isInputOK = true;
	static String fileName = "C:/data/郵便番号データ.csv";
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			AddressSearch app = new AddressSearch();
			app.setVisible(true);
		});
	}
	
	AddressSearch() {
		// フレームの基本設定
		super("住所検索");
		setSize(1000, 450);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// 画面中央に配置
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		
		//メニューの構築
		setJMenuBar(menuBar);
		menuBar.add(menuFile);
		menuFile.add(menuOpen);
		menuFile.add(menuExit);
		menuOpen.addActionListener(new MenuBarListener());
		menuExit.addActionListener(new MenuBarListener());
		
		// 左側の設定エリアを作成
		leftPanel = new leftPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.setBackground(new Color(200, 50, 50));
		// 検索方法のコンボボックス
		String[] searchMethods = {"郵便番号検索", "住所検索", "地名検索"};
		searchMethodComboBox = new JComboBox<>(searchMethods);
		searchMethodComboBox.addActionListener(new SearchMethodListener());
		// 読込データの表示
		selectedFile = new JTextField(20);
		selectedFile.setPreferredSize(new Dimension(1, 1));
		selectedFile.setText(fileName);
		selectedFile.setFont(new Font("ＭＳ ゴシック", Font.PLAIN, 14));
		selectedFile.setEnabled(false);
		// 入力フィールドと検索ボタンを作成
		textField1 = new JTextField(20);
		textField1.setPreferredSize(new Dimension(1, 1));
		textField1.setFont(new Font("ＭＳ ゴシック", Font.PLAIN, 14));
		textField2 = new JTextField(20);
		textField2.setPreferredSize(new Dimension(1, 1));
		textField2.setFont(new Font("ＭＳ ゴシック", Font.PLAIN, 14));
		// 初期設定：1ページあたり5件
		resultcntField = new JTextField(5);
		resultcntField.setPreferredSize(new Dimension(1, 1));
		resultcntField.setText("20");
		resultcntField.setFont(new Font("ＭＳ ゴシック", Font.PLAIN, 14));
		// 検索ボタンを設定
		searchButton = new JButton("検索");
		searchButton.addActionListener(new SearchButtonListener());
		
		// 左側パネルの制御
		var title = new JLabel("住所検索");
		title.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 32));
		title.setForeground(Color.white);
		// 左側パネルのタイトル
		leftPanel.add(title);
		leftPanel.add(new JLabel("　"));
		// 左側パネルの検索方法
		var searchinfo = new JLabel("▼ 検索方法を選択");
		searchinfo.setFont(new Font("ＭＳ ゴシック", Font.PLAIN, 14));
		searchinfo.setForeground(Color.white);
		leftPanel.add(searchinfo);
		leftPanel.add(searchMethodComboBox);
		leftPanel.add(new JLabel("　"));
		// 左側パネルの検索項目1
		leftPanel.add(word1);
		word1.setFont(new Font("ＭＳ ゴシック", Font.PLAIN, 14));
		word1.setForeground(Color.white);
		leftPanel.add(textField1);
		leftPanel.add(new JLabel("　"));
		// 左側パネルの検索項目2
		leftPanel.add(word2);
		word2.setFont(new Font("ＭＳ ゴシック", Font.PLAIN, 14));
		word2.setForeground(Color.white);
		leftPanel.add(textField2);
		leftPanel.add(new JLabel("　"));
		// 左側パネルの表示ページ数
		var pageinfo = new JLabel("▼ 1ページあたりの表示件数");
		pageinfo.setFont(new Font("ＭＳ ゴシック", Font.PLAIN, 14));
		pageinfo.setForeground(Color.white);
		leftPanel.add(pageinfo);
		leftPanel.add(resultcntField);
		leftPanel.add(new JLabel("　"));
		// 左側パネルの読込データ表示
		var fileinfo = new JLabel("▼ 読込データ");
		fileinfo.setFont(new Font("ＭＳ ゴシック", Font.PLAIN, 14));
		fileinfo.setForeground(Color.white);
		leftPanel.add(fileinfo);
		selectedFile.setDisabledTextColor(Color.BLACK);
		leftPanel.add(selectedFile);
		leftPanel.add(new JLabel("　"));
		// 左側パネルの検索ボタン
		leftPanel.add(searchButton);
		
		// 右側パネルの制御
		rightPanel = new rightPanel();
		rightPanel.setLayout(new BorderLayout());
		rightPanel.setBackground(new Color(200, 50, 50));
		// 右側パネルのタイトル
		var titlePane = new rightPanel();
		titlePane.setLayout(new GridLayout(1, 2));
		resultTitle.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 18));
		resultTitle.setForeground(Color.white);
		titlePane.add(resultTitle);
		resultCall.setFont(new Font("ＭＳ ゴシック", Font.PLAIN, 14));
		resultCall.setForeground(Color.white);
		titlePane.add(resultCall);
		rightPanel.add(titlePane, BorderLayout.NORTH);
		// 右側パネルの検索結果エリア
		var resultArea = new JTextArea();
		resultArea.setFont(new Font("ＭＳ ゴシック", Font.PLAIN, 14));
		resultArea.setOpaque(false);
		resultArea.setEditable(false);
		var scrollPane = new JScrollPane(resultArea);
		rightPanel.add(scrollPane, BorderLayout.CENTER);
		// 右側パネルのページ送り・ページ戻しボタン
		var pagePanel = new rightPanel();
		var prevPageButton = new JButton("前へ");
		var nextPageButton = new JButton("次へ");
		prevPageButton.addActionListener(e -> changePage(-1));
		nextPageButton.addActionListener(e -> changePage(1));
		pagePanel.add(prevPageButton);
		pagePanel.add(nextPageButton);
		rightPanel.add(pagePanel, BorderLayout.SOUTH);
		
		// 左右にパネルを配置
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
		splitPane.setDividerLocation(300); // 左右の分割比率
		add(splitPane);
		
		// 初期設定
		setSearchMethod("郵便番号検索");
	}
	// パネル拡張・背景画像設定
	public class leftPanel extends JPanel {
		@Override
		public void paintComponent(Graphics g) {
			int w = bgimage.getWidth(this);
			int h = bgimage.getHeight(this);
			Dimension d = getSize();
			//画像を描画
			for(var i=0; i<=d.width; i+=w) {
				for(var j=0; j<=d.height; j+=h) {
					g.drawImage(bgimage, i, j, this);
				}
			}
			g.drawImage(image, 20, 3, this);
		}
	}
	public class rightPanel extends JPanel {
		@Override
		public void paintComponent(Graphics g) {
			int w = bgimage.getWidth(this);
			int h = bgimage.getHeight(this);
			Dimension d = getSize();
			//画像を描画
			for(var i=0; i<=d.width; i+=w) {
				for(var j=0; j<=d.height; j+=h) {
					g.drawImage(bgimage, i, j, this);
				}
			}
			g.drawImage(poskuma, d.width-500, d.height-500, this);
		}
	}
	// メニューのリスナー
	private class MenuBarListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == menuOpen) {
				JFileChooser filechooser = new JFileChooser();
				int selected = filechooser.showOpenDialog(new JPanel());
				
				if (selected == JFileChooser.APPROVE_OPTION){
					File file = filechooser.getSelectedFile();
					fileName = String.format(file.getAbsolutePath());
					selectedFile.setText(fileName);
				}
			}
			else if(e.getSource() == menuExit) {
				System.exit(0);
			}
		}
	}
	// 検索方法の変更時に表示する入力フィールドを更新するリスナー
	private class SearchMethodListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String selectedMethod = (String) searchMethodComboBox.getSelectedItem();
			setSearchMethod(selectedMethod);
		}
	}
	// 検索ボタンのリスナー
	private class SearchButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			input1 = textField1.getText().trim();
			input2 = textField2.getText().trim();
			try {
				perpage = Integer.parseInt(resultcntField.getText().trim());
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(AddressSearch.this, "無効な表示件数です。\n数字で指定してください。");
				return;
			}
			ss.init();
			//ファイルを開きデータの取得が完了したかどうか
			if(isInputOK == true) {
				searchResults.clear();
				ss.search();
				if(isInputOK == false) {
					JOptionPane.showMessageDialog(AddressSearch.this, "不正なデータです。\n読込ファイルを確認してください。");
				}
				else{
					ss.sort();
					currentPage = 0;
					updateResultArea();
				}
			}
			else {
				JOptionPane.showMessageDialog(AddressSearch.this, "不正な検索値です。\n検索方法を確認してください。");
			}
		}
	}
	
	// 検索方法に応じて入力フィールドを更新・検索システムをセット
	private void setSearchMethod(String method) {
		textField1.setText("");
		textField2.setText("");
		if(method.equals("郵便番号検索")) {
			ss = new CodeSearch();
			word1.setText("▼ 郵便番号を入力 （前方一致）");
			word2.setText("－");
			textField1.setEnabled(true);
			textField2.setEnabled(false);
		}
		else if(method.equals("住所検索")) {
			ss = new Han2Search();
			word1.setText("▼ 都道府県を入力 （前方一致）");
			word2.setText("▼ 市区町村から入力 （前方一致）");
			textField1.setEnabled(true);
			textField2.setEnabled(true);
		}
		else if(method.equals("地名検索")) {
			ss = new Han1Search();
			word1.setText("▼ 地名を入力 （部分一致）");
			word2.setText("－");
			textField1.setEnabled(true);
			textField2.setEnabled(false);
		}
	}
	// ページ変更の処理
	private void changePage(int diff) {
		int totalPages = (int) Math.ceil((double) searchResults.size() / perpage);
		currentPage = Math.max(0, Math.min(currentPage + diff, totalPages - 1));
		updateResultArea();
	}
	// 検索結果エリアを更新
	private void updateResultArea() {
		JTextArea resultArea = (JTextArea) ((JScrollPane) rightPanel.getComponent(1)).getViewport().getView();
		resultArea.setText("");
		int start = 0;
		int end = 0;
		if(searchResults.size() == 0) {
			resultArea.append("　\n");
			resultArea.append("　　　該当なし\n");
			resultCall.setText("表示件数：0");
		}
		else {
			start = currentPage * perpage;
			end = Math.min(start + perpage, searchResults.size());
			resultArea.append("　\n");
			//配列に変換
			var array = new Data[searchResults.size()];
			searchResults.toArray(array);
			//表示
			for(int i = start; i < end; i++) {
				resultArea.append(String.format("　　%4d", i+1)+"） "+array[i].toString() + "\n");
			}
			resultCall.setText("表示件数："+(start+1)+"～"+end+"／検索件数："+searchResults.size());
		}
	}
}