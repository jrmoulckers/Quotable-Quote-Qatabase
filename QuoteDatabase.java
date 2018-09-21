import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class QuoteDatabase extends Application {

	private ArrayList<Quote> myQuotes = new ArrayList<>();
	private TreeMap<String, HashSet<Quote>> quoteKeywords = new TreeMap<String, HashSet<Quote>>();
	private TreeMap<String, HashSet<Quote>> quoteTopics = new TreeMap<String, HashSet<Quote>>();
	private TreeMap<String, HashSet<Quote>> quoteDates = new TreeMap<String, HashSet<Quote>>();
	private TreeMap<String, HashSet<Quote>> quoteAuthors = new TreeMap<String, HashSet<Quote>>();
	private File file;
	
	@Override
public void start(Stage stage) throws Exception {	
	BorderPane rootPane = new BorderPane();
	
	MenuBar menuBar = new MenuBar();
	
	Menu fileMenu = new Menu("File");
	
	MenuItem loadItem = new MenuItem("Load...");
	fileMenu.getItems().add(loadItem);
	loadItem.setOnAction(e ->{
		System.out.println("Loading...");
		load(stage);
	});
	
	MenuItem saveItem = new MenuItem("Save");
	fileMenu.getItems().add(saveItem);
	saveItem.setOnAction(e ->{
		System.out.println("Saving...");
		save();
	});
	
	MenuItem exitItem = new MenuItem("Exit");
	fileMenu.getItems().add(exitItem);
	exitItem.setOnAction(e -> {
		System.exit(0);
	});
	
	Menu helpMenu = new Menu("Help");
	
	MenuItem aboutItem = new MenuItem("About");
	helpMenu.getItems().add(aboutItem);
	aboutItem.setOnAction(e ->{
		System.out.println("About...");
	});
	
	
	menuBar.getMenus().add(fileMenu);
	menuBar.getMenus().add(helpMenu);
	
	BorderPane contents = new BorderPane();
	
	HBox titleBox = new HBox();
	
	Label title = new Label("Quotable");
	title.setFont(Font.font("Arial", 90));
	
	titleBox.getChildren().add(title);
	titleBox.setAlignment(Pos.CENTER);
	titleBox.setPrefHeight(110);
	
	GridPane selection = new GridPane();
	
	HBox addQuoteBox = new HBox();
	HBox orBox = new HBox();
	HBox subtitleBox = new HBox();
	HBox buttonBox = new HBox();
	
	Button addQuoteButton = new Button("ADD A QUOTE!");
	addQuoteButton.setFont(Font.font("Arial", 50));
	addQuoteButton.setMinSize(180, 90);
	
	addQuoteButton.setOnAction(e -> {
		addQuote(stage);
	});
	
	Label subtitle = new Label("Search for a quote:");
	subtitle.setFont(Font.font("Arial", 50));
	
	Label orText = new Label("or");
	orText.setFont(Font.font("or", 30));
	
	Button searchA = new Button("Author");
	Button searchK = new Button("Keywords");
	Button searchD = new Button("Date");
	Button searchT = new Button("Topic");
	
	searchA.setMinSize(100, 50);
	searchA.setFont(Font.font("Arial", 20));
	
	searchK.setMinSize(100, 50);
	searchK.setFont(Font.font("Arial", 20));
	
	searchD.setMinSize(100, 50);
	searchD.setFont(Font.font("Arial", 20));
	
	searchT.setMinSize(100, 50);
	searchT.setFont(Font.font("Arial", 20));
	
	searchA.setOnAction(e -> {
		searchAuthorScene(stage);
	});

	searchK.setOnAction(e -> {
		searchKeywordScene(stage);
	});
	
	searchD.setOnAction(e -> {
		searchDateScene(stage);
	});
	
	searchT.setOnAction(e -> {
		searchTopicScene(stage);
	});
	
	GridPane g = new GridPane();
	g.setPrefHeight(160);
	g.setAlignment(Pos.TOP_CENTER);
	if(!myQuotes.isEmpty()){
		Random r = new Random();
		Quote q = myQuotes.get(r.nextInt(myQuotes.size() - 1));
		
		
		GridPane featuredQuote = getQuoteFormat(q, q.getAuthor(), q.getDate(), q.getTopic());
		
		Label fQuote = new Label("Featured Quote: ");
		GridPane.setHalignment(fQuote, HPos.CENTER);
		fQuote.setFont(Font.font("Arial", 36));
		
		g.add(fQuote, 0, 0);
		g.add(featuredQuote, 0, 1);
	}
	else {
		Quote q = new Quote("Dr. Seuss", "Don’t cry because it’s over, smile because it happened.", "Date Unknown", "Happiness");
		
		GridPane featuredQuote = new GridPane();
		featuredQuote.setAlignment(Pos.CENTER);
		featuredQuote.setPrefHeight(80);
		featuredQuote.setHgap(20);
		
		Label quote = new Label("\"" + q.getQuote() + "\"\t");
		quote.setFont(Font.font("Arial", 16));
		featuredQuote.add(quote, 0, 0);
		
		Label author = new Label(q.getAuthor());
		author.setFont(Font.font("Arial", 16));
		featuredQuote.add(author, 1, 0);

		Label date = new Label("(" + q.getDate() + ")");
		date.setFont(Font.font("Arial", 16));
		featuredQuote.add(date, 2, 0);

		Label topic = new Label("Topic: " + q.getTopic());
		topic.setFont(Font.font("Arial", 16));
		featuredQuote.add(topic, 3, 0);

		
		Label fQuote = new Label("Featured Quote: ");
		GridPane.setHalignment(fQuote, HPos.CENTER);
		fQuote.setFont(Font.font("Arial", 36));
		
		g.add(fQuote, 0, 0);
		g.add(featuredQuote, 0, 1);
	}
	
	addQuoteBox.setAlignment(Pos.TOP_CENTER);
	addQuoteBox.setSpacing(100);
	addQuoteBox.getChildren().add(addQuoteButton);
	
	orBox.setAlignment(Pos.CENTER);
	orBox.getChildren().add(orText);
	
	buttonBox.setAlignment(Pos.CENTER);
	buttonBox.setSpacing(50);
	buttonBox.setPrefHeight(100);

	buttonBox.getChildren().addAll(searchK, searchA, searchD, searchT);

	subtitleBox.getChildren().add(subtitle);
	subtitleBox.setAlignment(Pos.CENTER);
	
	HBox loadFileBox = new HBox();
	loadFileBox.setPrefHeight(80);
	
	Button loadFile = new Button("Load Existing Quotes");
	loadFile.setFont(Font.font("Arial", 25));
	loadFile.setPrefHeight(30);
	loadFile.setOnAction(e -> {
		try {
			Scanner scan = new Scanner(new File("QuoteSource.txt"));
				while (scan.hasNext()) {
					String[] data = scan.nextLine().split("\\t");
					Quote q = new Quote(data[0], data[1], data[2], data[3]);
					
					if(data[1].length() > 0) {
						myQuotes.add(q);
						addAuthor(q);
						addTopic(q);
						addDate(q);
						addKeywords(q);
					}
				}
				fileLoaded(stage);
				scan.close();
			}
		catch (FileNotFoundException e1) {
			notFoundScene("File", "");
		}
	});
	
	loadFileBox.setAlignment(Pos.TOP_CENTER);
	loadFileBox.getChildren().add(loadFile);
	
	selection.add(g, 0, 0);
	selection.add(loadFileBox, 0, 1);
	selection.add(addQuoteBox, 0, 2);
	selection.add(orBox, 0, 3);
	selection.add(subtitleBox, 0, 4);
	selection.add(buttonBox, 0, 5);
	selection.setAlignment(Pos.CENTER);
	
	contents.setTop(titleBox);
	contents.setCenter(selection);
	
	rootPane.setTop(menuBar);
	rootPane.setCenter(contents);
	
	Scene homeScene= new Scene(rootPane, 1000, 700);
	
	addQuoteButton.requestFocus();
	
	stage.setScene(homeScene);
	stage.setTitle("Quotable");
	stage.show();
}
	
public void addQuote(Stage stage) {
	
	BorderPane rootPane = new BorderPane();
	
	MenuBar menuBar = new MenuBar();

	Menu fileMenu = new Menu("File");
	
	MenuItem loadItem = new MenuItem("Load...");
	fileMenu.getItems().add(loadItem);
	loadItem.setOnAction(e ->{
		System.out.println("Loading...");
		load(stage);
	});
	
	MenuItem saveItem = new MenuItem("Save");
	fileMenu.getItems().add(saveItem);
	saveItem.setOnAction(e ->{
		System.out.println("Saving...");
		save();
	});
	
	MenuItem exitItem = new MenuItem("Exit");
	fileMenu.getItems().add(exitItem);
	exitItem.setOnAction(e -> {
		System.exit(0);
	});
	
	Menu helpMenu = new Menu("Help");
	
	MenuItem aboutItem = new MenuItem("About");
	helpMenu.getItems().add(aboutItem);
	aboutItem.setOnAction(e ->{
		System.out.println("About...");
	});
	
	menuBar.getMenus().add(fileMenu);
	menuBar.getMenus().add(helpMenu);
	
	BorderPane contents = new BorderPane(); 
	
	HBox titleBox = new HBox();
	
	Label title = new Label("Quotable");
	title.setFont(Font.font("Arial", 90));
	
	titleBox.getChildren().add(title);
	titleBox.setAlignment(Pos.CENTER);
	titleBox.setPrefHeight(150);
	
	GridPane lowGrid = new GridPane();
	lowGrid.setAlignment(Pos.CENTER);
	
	GridPane grid = new GridPane();
	grid.setAlignment(Pos.CENTER);
	
	Label quoteLabel = new Label("Quote:");
	Label authorLabel = new Label("Author:");
	Label dateLabel = new Label("Date:");
	Label topicLabel = new Label("Topic:");
	
	quoteLabel.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 25));
	authorLabel.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 25));
	dateLabel.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 25));
	topicLabel.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 25));
	
	TextArea quoteArea = new TextArea();
	TextField authorField = new TextField();
	TextField dateField = new TextField();
	TextField topicField = new TextField();

	
	quoteArea.setPromptText("What they said... (ex.: A joke is a very serious thing.");
	authorField.setPromptText("Who said it... (ex.: Winston Churchill)");
	dateField.setPromptText("mm/dd/yy (ex.: 03/29/2000)");
	topicField.setPromptText("Theme... (ex.: War, Politics, Womens' Rights, etc.)");
	
	
	quoteArea.setFont(quoteLabel.getFont());
	authorField.setFont(Font.font("Arial", 20));
	dateField.setFont(Font.font("Arial", 20));
	topicField.setFont(Font.font("Arial", 20));

	quoteArea.setPrefColumnCount(20);
	quoteArea.setPrefRowCount(5);

	grid.add(quoteLabel, 0, 0);
	grid.add(authorLabel, 0, 1);
	grid.add(quoteArea, 1, 0);
	grid.add(authorField, 1, 1);
	grid.add(dateLabel, 0, 2);
	grid.add(topicLabel, 0, 3);
	grid.add(dateField, 1, 2);
	grid.add(topicField, 1, 3);


	HBox buttons = new HBox();
	buttons.setAlignment(Pos.CENTER);
	buttons.setSpacing(10);
	buttons.setPrefHeight(100);
	
	HBox homeButton = new HBox();
	homeButton.setAlignment(Pos.CENTER);
	homeButton.setPrefHeight(20);
	
	HBox space = new HBox();
	space.setPrefHeight(30);
	
	Button submit = new Button("Submit");
	Button reset = new Button("Reset");
	Button home = new Button("Home");
	
	lowGrid.add(buttons, 0, 0);
	lowGrid.add(homeButton, 0, 1);
	lowGrid.add(space, 0, 2);
	
	reset.setOnAction(e -> {
		quoteArea.setText("");
		authorField.setText("");
		dateField.setText("");
		topicField.setText("");
		home.requestFocus();
	});
	
	submit.setOnAction(e -> {
		if(quoteArea.getText().length() > 0) {
			if(authorField.getText().length() == 0) {
				authorField.setText("Anonymous");
			}
			if(dateField.getText().length() == 0) {
				dateField.setText("Date Unknown");
			}
			if(topicField.getText().length() == 0) {
				topicField.setText("General");
			}				
				Quote q = new Quote(authorField.getText(), quoteArea.getText(), dateField.getText(), topicField.getText());
				
				myQuotes.add(q);
				addAuthor(q);
				addDate(q);
				addTopic(q);
				addKeywords(q);	
				
				quoteArea.clear();
				authorField.clear();
				dateField.clear();
				topicField.clear();

				home.requestFocus();
			}	
	});
	
	home.setOnAction(e -> {
		try {
			start(stage);
		} catch (Exception e1) {
		}
	});
	
	quoteArea.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
		if(e.getCode() == KeyCode.TAB) {
			authorField.requestFocus();
			e.consume();
		}
	});
			
	submit.setPrefSize(100, 30);
	reset.setPrefSize(100, 30);
	home.setPrefSize(100, 30);
	
	submit.setFont(Font.font("Arial", 15));
	reset.setFont(Font.font("Arial", 15));
	home.setFont(Font.font("Arial", 15));
	
	buttons.getChildren().addAll(submit, reset);
	homeButton.getChildren().addAll(home);
	
	contents.setTop(titleBox);
	contents.setCenter(grid);
	contents.setBottom(lowGrid);
	
	rootPane.setTop(menuBar);
	rootPane.setCenter(contents);
	
	Scene addQuoteScene = new Scene(rootPane, 1000, 700);
	
	home.requestFocus();
	
	stage.setScene(addQuoteScene);
	stage.setTitle("Add a Quote");
	stage.show();
}
	
	public void load(Stage stage) {
		myQuotes.clear();
		quoteAuthors.clear();
		quoteKeywords.clear();
		quoteDates.clear();
		quoteTopics.clear();
		
		FileChooser chooser = new FileChooser();
		file = chooser.showOpenDialog(null);
		
		if(file == null) {
			return;
		}
		
		try {
			Scanner scan = new Scanner(file);
			while (scan.hasNext()) {
				String[] data = scan.nextLine().split("\\t");
				Quote q = new Quote(data[0], data[1], data[2], data[3]);
				
				if(data[1].length() > 0) {
					myQuotes.add(q);
					addAuthor(q);
					addTopic(q);
					addDate(q);
					addKeywords(q);
				}
			}
			fileLoaded(stage);
			scan.close();		
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void save() {
		
		if (file == null) {
			FileChooser chooser = new FileChooser();
			file = chooser.showSaveDialog(null);
			
			if (file == null) {
				return;
			}
		}
		
		
		try {
			PrintWriter fout = new PrintWriter(file);
			
			for(Quote q : myQuotes) {
				q.setQuoteBack();
				fout.println(q);
			}
			
			fout.close();
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void addAuthor(Quote q) {
		if(quoteAuthors.get(q.getAuthor()) == null) {
			HashSet<Quote> x = new HashSet<>();
			x.add(q);
			quoteAuthors.put(q.getAuthor(), x);
		}
		else
			quoteAuthors.get(q.getAuthor()).add(q);
	}
	
	public void addDate(Quote q) {
			if(quoteDates.get(q.getDate()) == null) {
				HashSet<Quote> x = new HashSet<>();
				x.add(q);
				quoteDates.put(q.getDate(), x);
			}
			else
				quoteDates.get(q.getDate()).add(q);
	}
	
	public void addTopic(Quote q) {
		if(q.getTopic() != null) {
			if(quoteTopics.get(q.getTopic()) == null) {
				HashSet<Quote> x = new HashSet<>();
				x.add(q);
				quoteTopics.put(q.getTopic(), x);
			}
			else
				quoteTopics.get(q.getTopic()).add(q);
		}	
	}
	
	public void addKeywords(Quote q) {
		if(q.getKeywords() != null) {
			for(String x : q.getKeywords()) {
				if(quoteKeywords.get(x) == null) {
					HashSet<Quote> y = new HashSet<>();
					y.add(q);
					quoteKeywords.put(x, y);
				}
				else
					quoteKeywords.get(x).add(q);
			}
		}	
	}

	public void searchAuthorScene(Stage stage) {
		BorderPane rootPane = new BorderPane();
		
		MenuBar menuBar = new MenuBar();
		
		Menu fileMenu = new Menu("File");
		
		MenuItem loadItem = new MenuItem("Load...");
		fileMenu.getItems().add(loadItem);
		loadItem.setOnAction(e ->{
			System.out.println("Loading...");
			load(stage);
		});
		
		MenuItem saveItem = new MenuItem("Save");
		fileMenu.getItems().add(saveItem);
		saveItem.setOnAction(e ->{
			System.out.println("Saving...");
			save();
		});
		
		MenuItem exitItem = new MenuItem("Exit");
		fileMenu.getItems().add(exitItem);
		exitItem.setOnAction(e -> {
			System.exit(0);
		});
		
		Menu helpMenu = new Menu("Help");
		
		MenuItem aboutItem = new MenuItem("About");
		helpMenu.getItems().add(aboutItem);
		aboutItem.setOnAction(e ->{
			System.out.println("About...");
		});
		
		menuBar.getMenus().add(fileMenu);
		menuBar.getMenus().add(helpMenu);
		
		BorderPane contents = new BorderPane();
		
		HBox titleBox = new HBox();
		
		Label title = new Label("Quotable");
		title.setFont(Font.font("Arial", 90));
		
		titleBox.getChildren().add(title);
		titleBox.setAlignment(Pos.CENTER);
		titleBox.setPrefHeight(150);

		
		GridPane selection = new GridPane();
		HBox homeButton = new HBox();
		homeButton.setAlignment(Pos.TOP_CENTER);
		
		GridPane searchGrid = new GridPane(); 
		HBox subtitleBox = new HBox();
		HBox orBox = new HBox();
		HBox buttonBox = new HBox();
		
		HBox searchBarBox = new HBox();
		
		TextField searchBar = new TextField();
		
		searchBar.setAlignment(Pos.CENTER);
		searchBar.setPromptText("Authors... (ex.: Winston Churchill)");
		searchBar.setFont(Font.font("Arial", 20));
		searchBar.setPrefColumnCount(18);

		searchBarBox.setAlignment(Pos.CENTER);
		searchBarBox.getChildren().add(searchBar);
		searchBarBox.setPrefWidth(390);
		
		Label subtitle = new Label("Search for an author:");
		subtitle.setFont(Font.font("Arial", 50));
		
		Label orText = new Label("or");
		orText.setFont(Font.font("or", 30));
		
		Button select = new Button("See a list of authors...");
		Button search = new Button("Search");
		
		Button home = new Button("Home");
		home.setPrefSize(100, 30);
		home.setFont(Font.font("Arial", 15));
		
		select.setMinSize(150, 50);
		select.setFont(Font.font("Arial", 20));
		
		search.setMinSize(120, 30);
		search.setFont(Font.font("Arial", 20));		
		
		select.setOnAction(e -> {
			authorList();
			searchBar.clear();
			home.requestFocus();
		});
		
		search.setOnAction(e -> {
			if(!searchBar.getText().equals("")) {
				searchAuthor(searchBar.getText());
				searchBar.clear();
				home.requestFocus();
			}
		});
		
		home.setOnAction(e -> {
			try {
				start(stage);
			} catch (Exception e1) {
				
			}
		});
		
		searchBar.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
			if(e.getCode() == KeyCode.ENTER) {
				searchAuthor(searchBar.getText());
				searchBar.clear();
				home.requestFocus();
				e.consume();
			}
		});
		
		searchGrid.setAlignment(Pos.CENTER);
		searchGrid.setPrefHeight(90);
		searchGrid.add(searchBarBox, 0, 0);
		searchGrid.add(search, 1, 0);

		orBox.setAlignment(Pos.CENTER);
		orBox.setPrefHeight(10);
		orBox.getChildren().add(orText);
		
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.setSpacing(50);
		buttonBox.setPrefHeight(100);
		buttonBox.getChildren().addAll(select);

		subtitleBox.getChildren().add(subtitle);
		subtitleBox.setAlignment(Pos.TOP_CENTER);
		
		selection.add(subtitleBox, 0, 0);
		selection.add(searchGrid, 0, 1);
		selection.add(orBox, 0, 3);
		selection.add(buttonBox, 0, 4);
		selection.setAlignment(Pos.CENTER);
		
		homeButton.getChildren().add(home);
		homeButton.setPrefHeight(100);
		
		
		contents.setTop(titleBox);
		contents.setCenter(selection);
		contents.setBottom(homeButton);
		
		rootPane.setTop(menuBar);
		rootPane.setCenter(contents);

		Scene authorScene= new Scene(rootPane, 1000, 700);
		
		home.requestFocus();
		
		stage.setScene(authorScene);
		stage.setTitle("Search for Authors");
		stage.show();
	}
	
	public void searchDateScene(Stage stage) {
		BorderPane rootPane = new BorderPane();
		
		MenuBar menuBar = new MenuBar();
		
		Menu fileMenu = new Menu("File");
		
		MenuItem loadItem = new MenuItem("Load...");
		fileMenu.getItems().add(loadItem);
		loadItem.setOnAction(e ->{
			System.out.println("Loading...");
			load(stage);
		});
		
		MenuItem saveItem = new MenuItem("Save");
		fileMenu.getItems().add(saveItem);
		saveItem.setOnAction(e ->{
			System.out.println("Saving...");
			save();
		});
		
		MenuItem exitItem = new MenuItem("Exit");
		fileMenu.getItems().add(exitItem);
		exitItem.setOnAction(e -> {
			System.exit(0);
		});
		
		Menu helpMenu = new Menu("Help");
		
		MenuItem aboutItem = new MenuItem("About");
		helpMenu.getItems().add(aboutItem);
		aboutItem.setOnAction(e ->{
			System.out.println("About...");
		});
		
		
		menuBar.getMenus().add(fileMenu);
		menuBar.getMenus().add(helpMenu);
		
		BorderPane contents = new BorderPane();
		
		HBox titleBox = new HBox();
		
		Label title = new Label("Quotable");
		title.setFont(Font.font("Arial", 90));
		
		titleBox.getChildren().add(title);
		titleBox.setAlignment(Pos.CENTER);
		titleBox.setPrefHeight(150);

		
		GridPane selection = new GridPane();
		HBox homeButton = new HBox();
		homeButton.setAlignment(Pos.TOP_CENTER);
		
		GridPane searchGrid = new GridPane();
		HBox subtitleBox = new HBox();
		
		HBox searchBarBox = new HBox();
		
		TextField searchBar = new TextField();
		
		searchBar.setAlignment(Pos.CENTER);
		searchBar.setPromptText("mm/dd/yy (ex.: 03/29/2000)");
		searchBar.setFont(Font.font("Arial", 20));
		searchBar.setPrefColumnCount(18);

		searchBarBox.setAlignment(Pos.CENTER);
		searchBarBox.getChildren().add(searchBar);
		searchBarBox.setPrefWidth(390);
		
		Label subtitle = new Label("Search for a date:");
		subtitle.setFont(Font.font("Arial", 50));
		
		Button search = new Button("Search");
		
		Button home = new Button("Home");
		home.setPrefSize(100, 30);
		home.setFont(Font.font("Arial", 15));
		
		search.setMinSize(120, 30);
		search.setFont(Font.font("Arial", 20));		
		
		search.setOnAction(e -> {
			if(!searchBar.getText().equals("")) {
				searchDate(searchBar.getText());
				searchBar.clear();
				home.requestFocus();
			}
		});
		
		home.setOnAction(e -> {
			try {
				start(stage);
			} catch (Exception e1) {
				
			}
		});
		
		searchBar.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
			if(e.getCode() == KeyCode.ENTER) {
				searchDate(searchBar.getText());
				searchBar.clear();
				home.requestFocus();
				e.consume();
			}
		});
		
		searchGrid.setAlignment(Pos.CENTER);
		searchGrid.setPrefHeight(90);
		searchGrid.add(searchBarBox, 0, 0);
		searchGrid.add(search, 1, 0);

		subtitleBox.getChildren().add(subtitle);
		subtitleBox.setAlignment(Pos.TOP_CENTER);
		
		selection.add(subtitleBox, 0, 0);
		selection.add(searchGrid, 0, 1);
		selection.setAlignment(Pos.CENTER);
		
		homeButton.getChildren().add(home);
		homeButton.setPrefHeight(100);
		
		contents.setTop(titleBox);
		contents.setCenter(selection);
		contents.setBottom(homeButton);
		
		rootPane.setTop(menuBar);
		rootPane.setCenter(contents);

		Scene dateScene= new Scene(rootPane, 1000, 700);
		
		home.requestFocus();
		
		home.requestFocus();
		
		stage.setScene(dateScene);
		stage.setTitle("Search for Dates");
		stage.show();
	}

	public void searchTopicScene(Stage stage) {
		BorderPane rootPane = new BorderPane();
		
		MenuBar menuBar = new MenuBar();
		
		Menu fileMenu = new Menu("File");
		
		MenuItem loadItem = new MenuItem("Load...");
		fileMenu.getItems().add(loadItem);
		loadItem.setOnAction(e ->{
			System.out.println("Loading...");
			load(stage);
		});
		
		MenuItem saveItem = new MenuItem("Save");
		fileMenu.getItems().add(saveItem);
		saveItem.setOnAction(e ->{
			System.out.println("Saving...");
			save();
		});
		
		MenuItem exitItem = new MenuItem("Exit");
		fileMenu.getItems().add(exitItem);
		exitItem.setOnAction(e -> {
			System.exit(0);
		});
		
		Menu helpMenu = new Menu("Help");
		
		MenuItem aboutItem = new MenuItem("About");
		helpMenu.getItems().add(aboutItem);
		aboutItem.setOnAction(e ->{
			System.out.println("About...");
		});
		
		
		menuBar.getMenus().add(fileMenu);
		menuBar.getMenus().add(helpMenu);
		
		BorderPane contents = new BorderPane();
		
		HBox titleBox = new HBox();
		
		Label title = new Label("Quotable");
		title.setFont(Font.font("Arial", 90));
		
		titleBox.getChildren().add(title);
		titleBox.setAlignment(Pos.CENTER);
		titleBox.setPrefHeight(150);

		
		GridPane selection = new GridPane();
		HBox homeButton = new HBox();
		homeButton.setAlignment(Pos.TOP_CENTER);
		
		GridPane searchGrid = new GridPane();
		HBox subtitleBox = new HBox();
		HBox orBox = new HBox();
		HBox buttonBox = new HBox();
		
		HBox searchBarBox = new HBox();
		
		TextField searchBar = new TextField();
		
		searchBar.setAlignment(Pos.CENTER);
		searchBar.setPromptText("Topics... (ex.: War)");
		searchBar.setFont(Font.font("Arial", 20));
		searchBar.setPrefColumnCount(18);

		searchBarBox.setAlignment(Pos.CENTER);
		searchBarBox.getChildren().add(searchBar);
		searchBarBox.setPrefWidth(390);
		
		Label subtitle = new Label("Search for a topic:");
		subtitle.setFont(Font.font("Arial", 50));
		
		Label orText = new Label("or");
		orText.setFont(Font.font("or", 30));
		
		Button select = new Button("See a list of topics...");
		Button search = new Button("Search");
		
		Button home = new Button("Home");
		home.setPrefSize(100, 30);
		home.setFont(Font.font("Arial", 15));
		
		select.setMinSize(150, 50);
		select.setFont(Font.font("Arial", 20));
		
		search.setMinSize(120, 30);
		search.setFont(Font.font("Arial", 20));		
		
		select.setOnAction(e -> {
			topicList();
			searchBar.clear();
			home.requestFocus();
		});
		
		search.setOnAction(e -> {
			if(!searchBar.getText().equals("")) {
				searchTopic(searchBar.getText());
				searchBar.clear();
				home.requestFocus();
			}
		});
		
		home.setOnAction(e -> {
			try {
				start(stage);
			} catch (Exception e1) {
			}
		});
		
		searchBar.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
			if(e.getCode() == KeyCode.ENTER) {
				searchTopic(searchBar.getText());
				searchBar.clear();
				home.requestFocus();
				e.consume();
			}
		});
		
		searchGrid.setAlignment(Pos.CENTER);
		searchGrid.setPrefHeight(90);
		searchGrid.add(searchBarBox, 0, 0);
		searchGrid.add(search, 1, 0);

		orBox.setAlignment(Pos.CENTER);
		orBox.setPrefHeight(10);
		orBox.getChildren().add(orText);
		
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.setSpacing(50);
		buttonBox.setPrefHeight(100);
		buttonBox.getChildren().addAll(select);

		subtitleBox.getChildren().add(subtitle);
		subtitleBox.setAlignment(Pos.TOP_CENTER);
		
		selection.add(subtitleBox, 0, 0);
		selection.add(searchGrid, 0, 1);
		selection.add(orBox, 0, 3);
		selection.add(buttonBox, 0, 4);
		selection.setAlignment(Pos.CENTER);
		
		homeButton.getChildren().add(home);
		homeButton.setPrefHeight(100);
		
		contents.setTop(titleBox);
		contents.setCenter(selection);
		contents.setBottom(homeButton);
		
		rootPane.setTop(menuBar);
		rootPane.setCenter(contents);

		Scene topicScene= new Scene(rootPane, 1000, 700);
		
		home.requestFocus();
		
		stage.setScene(topicScene);
		stage.setTitle("Search for Topics");
		stage.show();
	}

	public void searchKeywordScene(Stage stage) {
		BorderPane rootPane = new BorderPane();
		
		MenuBar menuBar = new MenuBar();
		
		Menu fileMenu = new Menu("File");
		
		MenuItem loadItem = new MenuItem("Load...");
		fileMenu.getItems().add(loadItem);
		loadItem.setOnAction(e ->{
			System.out.println("Loading...");
			load(stage);
		});
		
		MenuItem saveItem = new MenuItem("Save");
		fileMenu.getItems().add(saveItem);
		saveItem.setOnAction(e ->{
			System.out.println("Saving...");
			save();
		});
		
		MenuItem exitItem = new MenuItem("Exit");
		fileMenu.getItems().add(exitItem);
		exitItem.setOnAction(e -> {
			System.exit(0);
		});
		
		Menu helpMenu = new Menu("Help");
		
		MenuItem aboutItem = new MenuItem("About");
		helpMenu.getItems().add(aboutItem);
		aboutItem.setOnAction(e ->{
			System.out.println("About...");
		});
		
		
		menuBar.getMenus().add(fileMenu);
		menuBar.getMenus().add(helpMenu);
		
		BorderPane contents = new BorderPane();
		
		HBox titleBox = new HBox();
		
		Label title = new Label("Quotable");
		title.setFont(Font.font("Arial", 90));
		
		titleBox.getChildren().add(title);
		titleBox.setAlignment(Pos.CENTER);
		titleBox.setPrefHeight(150);

		
		GridPane selection = new GridPane();
		HBox homeButton = new HBox();
		homeButton.setAlignment(Pos.TOP_CENTER);
		
		GridPane searchGrid = new GridPane();
		HBox subtitleBox = new HBox();
		HBox buttonBox = new HBox();
		
		HBox searchBarBox = new HBox();
		
		TextField searchBar = new TextField();
		
		searchBar.setAlignment(Pos.CENTER);
		searchBar.setPromptText("Keywords... (ex.: War, Churchill, 1990)");
		searchBar.setFont(Font.font("Arial", 20));
		searchBar.setPrefColumnCount(18);

		searchBarBox.setAlignment(Pos.CENTER);
		searchBarBox.getChildren().add(searchBar);
		searchBarBox.setPrefWidth(390);
		
		Label subtitle = new Label("Search for a keyword:");
		subtitle.setFont(Font.font("Arial", 50));
	
		Button search = new Button("Search");
		
		Button home = new Button("Home");
		home.setPrefSize(100, 30);
		home.setFont(Font.font("Arial", 15));
		
		search.setMinSize(120, 30);
		search.setFont(Font.font("Arial", 20));		
		
		search.setOnAction(e -> {
			if(!searchBar.getText().equals("")) {
				Set<String> s = quoteKeywords.keySet();
				boolean hasIt = false;
				for(String x : s) {
					if(x.contains(searchBar.getText()))
						hasIt = true;
				}
				
				if(hasIt == true) {
					showKeywordQuotes(searchBar.getText());
					searchBar.clear();
					home.requestFocus();
					e.consume();
				}
				else {
					notFoundScene("KEYWORD", "\"" + searchBar.getText() + "\"");
					searchBar.clear();
					home.requestFocus();
					e.consume();
				}
			}	
		});
		
		home.setOnAction(e -> {
			try {
				start(stage);
			} catch (Exception e1) {
			}
		});
		
		searchBar.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
			if(e.getCode() == KeyCode.ENTER) {
				if(!searchBar.getText().equals("")) {
					Set<String> s = quoteKeywords.keySet();
					boolean hasIt = false;
					for(String x : s) {
						if(x.contains(searchBar.getText())) {
							hasIt = true;
						}
					}
					
					if(hasIt == true) {
						showKeywordQuotes(searchBar.getText());
						searchBar.clear();
						home.requestFocus();
						e.consume();
					}
					else {
						notFoundScene("KEYWORD", "\"" + searchBar.getText() + "\"");
						searchBar.clear();
						home.requestFocus();
						e.consume();
					}
				}
			}
		});
		
		searchGrid.setAlignment(Pos.CENTER);
		searchGrid.setPrefHeight(90);
		searchGrid.add(searchBarBox, 0, 0);
		searchGrid.add(search, 1, 0);
		
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.setSpacing(50);
		buttonBox.setPrefHeight(100);

		subtitleBox.getChildren().add(subtitle);
		subtitleBox.setAlignment(Pos.TOP_CENTER);
		
		selection.add(subtitleBox, 0, 0);
		selection.add(searchGrid, 0, 1);
		selection.add(buttonBox, 0, 4);
		selection.setAlignment(Pos.CENTER);
		
		homeButton.getChildren().add(home);
		homeButton.setPrefHeight(100);
		
		contents.setTop(titleBox);
		contents.setCenter(selection);
		contents.setBottom(homeButton);
		
		rootPane.setTop(menuBar);
		rootPane.setCenter(contents);

		Scene topicScene= new Scene(rootPane, 1000, 700);
		
		home.requestFocus();
		
		stage.setScene(topicScene);
		stage.setTitle("Search for Keywords");
		stage.show();
	}
	
	public void authorList() {
		Stage popStage = new Stage();
		
		BorderPane rootPane = new BorderPane();
		
		BorderPane contents = new BorderPane();
		
		HBox titleBox = new HBox();
		
		Label title = new Label("Authors");
		title.setFont(Font.font("Arial", 50));
		
		titleBox.getChildren().add(title);
		titleBox.setAlignment(Pos.CENTER);
		titleBox.setPrefHeight(150);
		
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.TOP_CENTER);
		grid.setHgap(50);
		grid.setVgap(100);
		
		ScrollPane scrollable = new ScrollPane(grid);
		scrollable.setFitToWidth(true);
		scrollable.setPadding(new Insets(40));
		
		if(!quoteAuthors.isEmpty()) {	
			Set<String> s = quoteAuthors.keySet();
			ArrayList<String> m = new ArrayList<>();
			
			for(String x : s) {
				m.add(x);
			}
			
			String[] a = new String[m.size()];
			m.toArray(a);
					
				for(int x = 0; x < a.length; x++){
					String z = a[x];
					
					Button b = new Button(z);
					b.setFont(Font.font("Arial", 15));
					GridPane.setHalignment(b, HPos.CENTER);
					
					b.setOnAction(e-> {
						showAuthorQuotes(z);
						popStage.close();
					});
					
					grid.add(b, (x)%4, (x/4));
				}			
		}
		else {
			Label l = new Label("No authors found...");
			l.setFont(Font.font("Arial", 30));
			grid.add(l, 0, 0);
		}
		
		HBox leftSpace = new HBox();
		leftSpace.setPrefWidth(30);
		
		HBox rightSpace = new HBox();
		rightSpace.setPrefWidth(30);
		
		contents.setTop(titleBox);
		contents.setCenter(scrollable);
		contents.setLeft(leftSpace);
		contents.setLeft(rightSpace);
		
		rootPane.setCenter(contents);
		
		Scene authorListScene = new Scene(rootPane, 1100, 600);
		
		popStage.setScene(authorListScene);
		popStage.setTitle("Authors");
		popStage.show();
	}
	
	private void topicList() {
		Stage popStage = new Stage();
		
		BorderPane rootPane = new BorderPane();
		
		BorderPane contents = new BorderPane();
		
		HBox titleBox = new HBox();
		
		Label title = new Label("Topics");
		title.setFont(Font.font("Arial", 50));
		
		titleBox.getChildren().add(title);
		titleBox.setAlignment(Pos.CENTER);
		titleBox.setPrefHeight(150);
		
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.TOP_CENTER);
		grid.setHgap(50);
		grid.setVgap(100);
		
		if(!quoteTopics.isEmpty()) {	
			Set<String> s = quoteTopics.keySet();
			ArrayList<String> m = new ArrayList<>();
			
			for(String x : s) {
				m.add(x);
				popStage.close();
			}
			
			String[] a = new String[m.size()];
			m.toArray(a);
			
			for(int x = 0; x < a.length; x++){
				String z = a[x];
					
				Button b = new Button(z);
				b.setFont(Font.font("Arial", 15));
				GridPane.setHalignment(b, HPos.CENTER);
				
				b.setOnAction(e-> {
					showTopicQuotes(z);
					popStage.close();
				});
					
					grid.add(b, (x)%6, x/6);
			}
				
		}
		else {
			Label l = new Label("No topics found...");
			l.setFont(Font.font("Arial", 30));
			grid.add(l, 0, 0);
		}
		
		HBox leftSpace = new HBox();
		leftSpace.setPrefWidth(30);
		
		HBox rightSpace = new HBox();
		rightSpace.setPrefWidth(30);
		
		contents.setTop(titleBox);
		contents.setCenter(grid);
		contents.setLeft(leftSpace);
		contents.setRight(rightSpace);
		
		rootPane.setCenter(contents);
		
		Scene topicListScene = new Scene(rootPane, 1100, 600);
		
		popStage.setScene(topicListScene);
		popStage.setTitle("Topics");
		popStage.show();
	}

	private void notFoundScene(String type, String a) {
		Stage popup = new Stage();

		BorderPane rootPane = new BorderPane();
		
		BorderPane contents = new BorderPane();
				
		Label statement = new Label(type+ ": " + a + " not found...");
		statement.setFont(Font.font("Arial", 25));
		
		contents.setCenter(statement);
		
		rootPane.setCenter(contents);
		
		Scene notFoundScene = new Scene(rootPane, 500, 100);
		popup.setScene(notFoundScene);
		popup.setTitle("Error: Not Found");
		popup.show();
	}
	
	public void searchAuthor(String a) {
		Set<String> s = quoteAuthors.keySet();
		boolean match = true;
		boolean tempMatch = false;
		TreeSet<String> authors = new TreeSet<>();
		
		if(!(s.isEmpty())) {			
			for(String x : s) {
				if(x.toLowerCase().contains(a.toLowerCase())) {
					authors.add(x);
					tempMatch = true;
				}
			}	
			
			match = tempMatch;
		}
		else
			match = false;
		
		if(match == true){
			showAuthors(authors, a);
		}
		else
			notFoundScene("AUTHOR", "\"" + a + "\"");
	}
	
	public void searchDate(String d) {
		Set<String> s = quoteDates.keySet();
		boolean match = true;
		boolean tempMatch = false;
		TreeSet<String> dates = new TreeSet<>();
		
		if(!(s.isEmpty())) {			
			for(String x : s) {
				if(x.toLowerCase().contains(d.toLowerCase())) {
					dates.add(x);
					tempMatch = true;
				}
			}	
			
			match = tempMatch;
		}
		else
			match = false;
		
		if(match == true){
			showDates(dates, d);
		}
		else
			notFoundScene("DATE", "\"" + d + "\"");
	}

	public void searchTopic(String t) {
		Set<String> s = quoteTopics.keySet();
		boolean match = true;
		boolean tempMatch = false;
		TreeSet<String> topics = new TreeSet<>();
		
		if(!(s.isEmpty())) {			
			for(String x : s) {
				if(x.toLowerCase().contains(t.toLowerCase())) {
					topics.add(x);
					tempMatch = true;
				}
			}	
			
			match = tempMatch;
		}
		else
			match = false;
		
		if(match == true){
			showTopics(topics, t);
		}
		else
			notFoundScene("TOPIC", "\"" + t + "\"");
	}

	/*public void searchKeyword(String k) {
		Set<String> s = quoteKeywords.keySet();
		boolean match = true;
		boolean tempMatch = false;
		TreeSet<String> keywords = new TreeSet<>();
		
		if(!(s.isEmpty())) {			
			for(String x : s) {
				if(x.toLowerCase().contains(k.toLowerCase())) {
					keywords.add(x);
					tempMatch = true;
				}
			}	
			
			match = tempMatch;
		}
		else
			match = false;
		
		if(match == true){
			showKeywordQuotes(keywords, k);
		}
		else
			notFoundScene("KEYWORD", "\"" + k + "\"");
	}
	*/
	private GridPane getQuoteFormat(Quote q, String a, String d, String t) {
		GridPane g = new GridPane();
		g.setAlignment(Pos.CENTER);
		g.setPrefHeight(120);
		g.setHgap(20);
		
		Label quote = new Label("\"" + q.getQuote() + "\"\t");
		quote.setFont(Font.font("Arial", 16));
		
		Button author = new Button(a);
		author.setFont(Font.font("Arial", 12));
		author.setOnAction(e-> {
			showAuthorQuotes(a);
		});

		Button date = new Button(d);
		date.setFont(Font.font("Arial", 12));
		date.setOnAction(e-> {
			showDateQuotes(d);
		});
		
		Button topic = new Button("Topic: " + t);
		topic.setFont(Font.font("Arial", 12));
		topic.setOnAction(e-> {
			showTopicQuotes(t);
		});
		
		GridPane.setHalignment(quote, HPos.CENTER);
		g.add(quote, 0, 0);
		g.add(author, 1, 0);
		g.add(date, 2, 0);
		g.add(topic, 3, 0);
		
		return g;
	}
	
	public void showAuthorQuotes(String a) {
		Stage popup = new Stage();
	
		BorderPane rootPane = new BorderPane();
		
		BorderPane contents = new BorderPane();
				
		GridPane quotes = new GridPane();
		quotes.setAlignment(Pos.TOP_CENTER);
		
		HBox h = new HBox();
		h.setAlignment(Pos.CENTER);
		h.setPrefHeight(100);
		
		Label statement = new Label("Quotes by " + a);
		statement.setFont(Font.font("Arial", 30));
		statement.setAlignment(Pos.TOP_CENTER);
		
		Label l = new Label("Quote:");
		l.setFont(Font.font("Arial", 27));
		
		Label m = new Label("Author:");
		m.setFont(Font.font("Arial", 27));
		
		Label n = new Label("Date:");
		n.setFont(Font.font("Arial", 27));
		
		Label o = new Label("Topic:");
		o.setFont(Font.font("Arial", 27));
		
		HashSet<Quote> qSet = quoteAuthors.get(a);
		int x = 2;
		for(Quote q : qSet) {
			GridPane g = getQuoteFormat(q, q.getAuthor(), q.getDate(), q.getTopic());
			quotes.add(g, 0, x);
			x++;
		}
		
		h.getChildren().add(statement);
		
		BorderPane b = new BorderPane();
		b.setCenter(quotes);
		
		ScrollPane scrollable = new ScrollPane(b);
		scrollable.setPadding(new Insets(40));
		scrollable.setFitToWidth(true);
		
		contents.setCenter(scrollable);
		contents.setTop(h);
		
		rootPane.setCenter(contents);
		
		Scene showAuthorScene = new Scene(rootPane, 1100, 600);
		popup.setScene(showAuthorScene);
		popup.setTitle(a);
		popup.show();
	}

	public void showDateQuotes(String d) {
		Stage popup = new Stage();

		BorderPane rootPane = new BorderPane();
		
		BorderPane contents = new BorderPane();
				
		GridPane quotes = new GridPane();
		quotes.setAlignment(Pos.TOP_CENTER);
		
		HBox h = new HBox();
		h.setAlignment(Pos.CENTER);
		h.setPrefHeight(100);
		
		Label statement = new Label("Quotes from " + d);
		statement.setFont(Font.font("Arial", 30));
		statement.setAlignment(Pos.TOP_CENTER);
		
		Label l = new Label("Quote:");
		l.setFont(Font.font("Arial", 27));
		
		Label m = new Label("Author:");
		m.setFont(Font.font("Arial", 27));
		
		Label n = new Label("Date:");
		n.setFont(Font.font("Arial", 27));
		
		Label o = new Label("Topic:");
		o.setFont(Font.font("Arial", 27));
		
		HashSet<Quote> qSet = quoteDates.get(d);
		int x = 2;
		for(Quote q : qSet) {
			GridPane g = getQuoteFormat(q, q.getAuthor(), q.getDate(), q.getTopic());
			quotes.add(g, 0, x);
			x++;
		}
		
		h.getChildren().add(statement);
		
		BorderPane b = new BorderPane();
		b.setCenter(quotes);
		
		ScrollPane scrollable = new ScrollPane(b);
		scrollable.setPadding(new Insets(40));
		scrollable.setFitToWidth(true);
		
		contents.setCenter(scrollable);
		contents.setTop(h);
		
		rootPane.setCenter(contents);
		
		Scene showDateScene = new Scene(rootPane, 1100, 600);
		popup.setScene(showDateScene);
		popup.setTitle(d);
		popup.show();
	}
	
	public void showTopicQuotes(String t) {
		Stage popup = new Stage();

		BorderPane rootPane = new BorderPane();
		
		BorderPane contents = new BorderPane();
				
		GridPane quotes = new GridPane();
		quotes.setAlignment(Pos.TOP_CENTER);
		
		HBox h = new HBox();
		h.setAlignment(Pos.CENTER);
		h.setPrefHeight(100);
		
		Label statement = new Label("Quotes relating to " + t);
		statement.setFont(Font.font("Arial", 30));
		statement.setAlignment(Pos.TOP_CENTER);
		
		Label l = new Label("Quote:");
		l.setFont(Font.font("Arial", 27));
		
		Label m = new Label("Author:");
		m.setFont(Font.font("Arial", 27));
		
		Label n = new Label("Date:");
		n.setFont(Font.font("Arial", 27));
		
		Label o = new Label("Topic:");
		o.setFont(Font.font("Arial", 27));
		
		HashSet<Quote> qSet = quoteTopics.get(t);
		int x = 2;
		for(Quote q : qSet) {
			GridPane g = getQuoteFormat(q, q.getAuthor(), q.getDate(), q.getTopic());
			quotes.add(g, 0, x);
			x++;
		}
		
		h.getChildren().add(statement);
		
		BorderPane b = new BorderPane();
		b.setCenter(quotes);
		
		ScrollPane scrollable = new ScrollPane(b);
		scrollable.setPadding(new Insets(40));
		scrollable.setFitToWidth(true);
		
		contents.setCenter(scrollable);
		contents.setTop(h);
		
		rootPane.setCenter(contents);
		
		Scene showDateScene = new Scene(rootPane, 1100, 600);
		popup.setScene(showDateScene);
		popup.setTitle(t);
		popup.show();
	}
	
	public void showKeywordQuotes(String k) {
		Stage popup = new Stage();

		BorderPane rootPane = new BorderPane();
		
		BorderPane contents = new BorderPane();
				
		GridPane quotes = new GridPane();
		quotes.setAlignment(Pos.TOP_CENTER);
		
		HBox h = new HBox();
		h.setAlignment(Pos.CENTER);
		h.setPrefHeight(100);
		
		Label statement = new Label("Quotes relating to keyword: \"" + k + "\"");
		statement.setFont(Font.font("Arial", 30));
		statement.setAlignment(Pos.TOP_CENTER);
		
		Label l = new Label("Quote:");
		l.setFont(Font.font("Arial", 27));
		
		Label m = new Label("Author:");
		m.setFont(Font.font("Arial", 27));
		
		Label n = new Label("Date:");
		n.setFont(Font.font("Arial", 27));
		
		Label o = new Label("Topic:");
		o.setFont(Font.font("Arial", 27));
		
		Set<String> s =  quoteKeywords.keySet();
		int x = 2;
		for(String j : s) {
			if(j.contains(k)) {
				HashSet<Quote> qSet = quoteKeywords.get(j);
				for(Quote q : qSet) {
					GridPane g = getQuoteFormat(q, q.getAuthor(), q.getDate(), q.getTopic());
					quotes.add(g, 0, x);
					x++;
				}
			}
		}
		
		h.getChildren().add(statement);
		
		BorderPane b = new BorderPane();
		b.setCenter(quotes);
		
		ScrollPane scrollable = new ScrollPane(b);
		scrollable.setPadding(new Insets(40));
		scrollable.setFitToWidth(true);
		
		contents.setCenter(scrollable);
		contents.setTop(h);
		
		rootPane.setCenter(contents);
		
		Scene showDateScene = new Scene(rootPane, 1100, 600);
		popup.setScene(showDateScene);
		popup.setTitle(k);
		popup.show();
	}

	public void showAuthors(TreeSet<String> authors, String t) {
		Stage popStage = new Stage();
		
		BorderPane rootPane = new BorderPane();
		
		BorderPane contents = new BorderPane();
		
		HBox titleBox = new HBox();
		
		Label title = new Label("Authors relating to \"" + t + "\":");
		title.setFont(Font.font("Arial", 50));
		
		titleBox.getChildren().add(title);
		titleBox.setAlignment(Pos.CENTER);
		titleBox.setPrefHeight(150);
		
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.TOP_CENTER);
		grid.setHgap(50);
		grid.setVgap(100);
				
		String[] s = new String[authors.size()];
		s = authors.toArray(s);
		
		for(int x = 0; x < s.length; x++){
			String z = s[x];
			
			Button b = new Button(z);
			b.setFont(Font.font("Arial", 15));
			GridPane.setHalignment(b, HPos.CENTER);
			
			b.setOnAction(e-> {
				showAuthorQuotes(z);
			});
			
			grid.add(b, (x)%4, (x/4));
		}			
		
		HBox leftSpace = new HBox();
		leftSpace.setPrefWidth(30);
		
		HBox rightSpace = new HBox();
		rightSpace.setPrefWidth(30);
		
		BorderPane b = new BorderPane();
		b.setCenter(grid);
		
		ScrollPane scrollable = new ScrollPane(b);
		scrollable.setPadding(new Insets(40));
		scrollable.setFitToWidth(true);
			
		contents.setTop(titleBox);
		contents.setCenter(scrollable);
		contents.setLeft(leftSpace);
		contents.setLeft(rightSpace);
		
		rootPane.setCenter(contents);
		
		Scene authorListScene = new Scene(rootPane, 1100, 600);
		
		popStage.setScene(authorListScene);
		popStage.setTitle("Search for Authors: \"" + t + "\"");
		popStage.show();
	}
	
	public void showDates(TreeSet<String> dates, String t) {
		Stage popStage = new Stage();
		
		BorderPane rootPane = new BorderPane();
		
		BorderPane contents = new BorderPane();
		
		HBox titleBox = new HBox();
		
		Label title = new Label("Dates relating to \"" + t + "\":");
		title.setFont(Font.font("Arial", 50));
		
		titleBox.getChildren().add(title);
		titleBox.setAlignment(Pos.CENTER);
		titleBox.setPrefHeight(150);
		
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.TOP_CENTER);
		grid.setHgap(50);
		grid.setVgap(100);
				
		String[] s = new String[dates.size()];
		s = dates.toArray(s);
		
		for(int x = 0; x < s.length; x++){
			String z = s[x];
			
			Button b = new Button(z);
			b.setFont(Font.font("Arial", 15));
			GridPane.setHalignment(b, HPos.CENTER);
			
			b.setOnAction(e-> {
				showDateQuotes(z);
			});
			
			grid.add(b, (x)%4, (x/4));
		}			
		
		HBox leftSpace = new HBox();
		leftSpace.setPrefWidth(30);
		
		HBox rightSpace = new HBox();
		rightSpace.setPrefWidth(30);
		
		BorderPane b = new BorderPane();
		b.setCenter(grid);
		
		ScrollPane scrollable = new ScrollPane(b);
		scrollable.setPadding(new Insets(40));
		scrollable.setFitToWidth(true);
			
		contents.setTop(titleBox);
		contents.setCenter(scrollable);
		contents.setLeft(leftSpace);
		contents.setLeft(rightSpace);
		
		rootPane.setCenter(contents);
		
		Scene authorListScene = new Scene(rootPane, 1100, 600);
		
		popStage.setScene(authorListScene);
		popStage.setTitle("Search for Dates: \"" + t + "\"");
		popStage.show();
	}

	public void showTopics(TreeSet<String> topics, String t) {
		Stage popStage = new Stage();
		
		BorderPane rootPane = new BorderPane();
		
		BorderPane contents = new BorderPane();
		
		HBox titleBox = new HBox();
		
		Label title = new Label("Topics relating to \"" + t + "\":");
		title.setFont(Font.font("Arial", 50));
		
		titleBox.getChildren().add(title);
		titleBox.setAlignment(Pos.CENTER);
		titleBox.setPrefHeight(150);
		
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.TOP_CENTER);
		grid.setHgap(50);
		grid.setVgap(100);
				
		String[] s = new String[topics.size()];
		s = topics.toArray(s);
		
		for(int x = 0; x < s.length; x++){
			String z = s[x];
			
			Button b = new Button(z);
			b.setFont(Font.font("Arial", 15));
			GridPane.setHalignment(b, HPos.CENTER);
			
			b.setOnAction(e-> {
				showTopicQuotes(z);
			});
			
			grid.add(b, (x)%4, (x/4));
		}			
		
		HBox leftSpace = new HBox();
		leftSpace.setPrefWidth(30);
		
		HBox rightSpace = new HBox();
		rightSpace.setPrefWidth(30);
		
		BorderPane b = new BorderPane();
		b.setCenter(grid);
		
		ScrollPane scrollable = new ScrollPane(b);
		scrollable.setPadding(new Insets(40));
		scrollable.setFitToWidth(true);
			
		contents.setTop(titleBox);
		contents.setCenter(scrollable);
		contents.setLeft(leftSpace);
		contents.setLeft(rightSpace);
		
		rootPane.setCenter(contents);
		
		Scene authorListScene = new Scene(rootPane, 1100, 600);
		
		popStage.setScene(authorListScene);
		popStage.setTitle("Search for Topics: \"" + t + "\"");
		popStage.show();
	}
	
	private void fileLoaded(Stage stage) {
		Stage popup = new Stage();

		BorderPane rootPane = new BorderPane();
		
		BorderPane contents = new BorderPane();
				
		Label statement = new Label("File loaded successfully");
		statement.setFont(Font.font("Arial", 25));
		
		contents.setCenter(statement);
		
		rootPane.setCenter(contents);
		
		Scene notFoundScene = new Scene(rootPane, 500, 100);
		popup.setScene(notFoundScene);
		popup.setTitle("File Loades Success");
		popup.show();
		
		PauseTransition delay = new PauseTransition(Duration.seconds(1));
		delay.setOnFinished( event -> popup.close() );
		delay.play();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}


