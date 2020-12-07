package de.hft.gui;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import de.hft.algorithmn.CollisionDetection;
import de.hft.algorithmn.RapidlyExploringRandomTree;
import de.hft.algorithmn.SimpleProbabilisticRoadmapMethod;
import de.hft.algorithmn.StraightLine;
import de.hft.filereader.BMPFileReader;
import de.hft.objects.Edge;
import de.hft.objects.Point;
import de.hft.objects.Robot;
import de.hft.objects.Room;
import de.hft.objects.TreeNode;

public class MainWindow {

	private static final MainWindow MAIN_WINDOW = new MainWindow();

	List<Point> _straightLine;
	List<Point> _sPMRSolution;
	List<Point> _RTTSolution;

	boolean _startButtonClicked = false;
	boolean _endButtonClicked = false;
	boolean _isStartPositionSet = false;
	boolean _isEndPositionSet = false;
	int _startXY[] = new int[2];
	int _endXY[] = new int[2];

	private List<Room> _allRooms;
	private int _indexOfRoom = 0;

	private List<Robot> _allRobots;
	private int _indexOfRobot = 0;

	private static final String WINDOW_NAME = "Cleaning Robot";

	private Display _display = new Display();
	private Shell _shell = new Shell(_display);

	private Composite _leftParentComposite = new Composite(_shell, SWT.NONE);
	private Label _roomNameLabel = new Label(_leftParentComposite, SWT.NONE);
	private Button _roomLeftArrowButton = new Button(_leftParentComposite, SWT.ARROW | SWT.LEFT);
	private Button _roomtRightArrowButton = new Button(_leftParentComposite, SWT.ARROW | SWT.RIGHT);

	@SuppressWarnings("unused")
	private Label filler1 = new Label(_leftParentComposite, SWT.NONE);

	private Label _cleaningRobotNameLabel = new Label(_leftParentComposite, SWT.NONE);
	private Canvas _cleaningRobotCanvas = new Canvas(_leftParentComposite, SWT.BORDER);
	private Button _robotLeftArrowButton = new Button(_leftParentComposite, SWT.ARROW | SWT.LEFT);
	private Button _robotRightArrowButton = new Button(_leftParentComposite, SWT.ARROW | SWT.RIGHT);

	private Button _robotStartPositionButton = new Button(_leftParentComposite, SWT.PUSH);
	private Button _robotEndPositionButton = new Button(_leftParentComposite, SWT.PUSH);

	TabFolder _tabFolder = new TabFolder(_shell, SWT.NONE);
	TabItem _tabWorkspace = new TabItem(_tabFolder, SWT.NONE);
	TabItem _tabConfigurationSpace = new TabItem(_tabFolder, SWT.NONE);

	Group _groupWorkspace = new Group(_tabFolder, SWT.NONE);
	Group _groupConfigurationSpace = new Group(_tabFolder, SWT.NONE);

	private Canvas _roomMapWorkspace = new Canvas(_groupWorkspace, SWT.BORDER);
	private Canvas _roomMapConfigurationSpace = new Canvas(_groupConfigurationSpace, SWT.BORDER);

	private Label _label_X = new Label(_leftParentComposite, SWT.NONE);
	private Text _text_X = new Text(_leftParentComposite, SWT.BORDER);
	private Label _label_Y = new Label(_leftParentComposite, SWT.NONE);
	private Text _text_Y = new Text(_leftParentComposite, SWT.BORDER);

	private Combo _algorithmnComboBox = new Combo(_leftParentComposite, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);

	private Label _label_sPMRSample = new Label(_leftParentComposite, SWT.NONE);
	private Text _text_sPMRSample = new Text(_leftParentComposite, SWT.BORDER);
	private Label _label_sPMRRadius = new Label(_leftParentComposite, SWT.NONE);
	private Text _text_sPMRRadius = new Text(_leftParentComposite, SWT.BORDER);

	private Button _runSPMRButtonMultipleTimes = new Button(_leftParentComposite, SWT.PUSH);
	private Text _text_SPMRButtonMultipleTime = new Text(_leftParentComposite, SWT.BORDER | SWT.CENTER);

	private Label _label_sPMRSuccessRate = new Label(_leftParentComposite, SWT.NONE);
	private Text _text_sPMRSuccessRate = new Text(_leftParentComposite, SWT.BORDER | SWT.READ_ONLY);

	private Label _rrtTimeLabel = new Label(_leftParentComposite, SWT.NONE);
	private Text _rrtTimeText = new Text(_leftParentComposite, SWT.BORDER);

	private Label _rrtRadiusLabel = new Label(_leftParentComposite, SWT.NONE);
	private Text _rrtRadiusText = new Text(_leftParentComposite, SWT.BORDER);

	private Button _resetButton = new Button(_leftParentComposite, SWT.PUSH);

	private Slider _slider = new Slider(_leftParentComposite, SWT.HORIZONTAL);

	private MainWindow() {
		_allRooms = BMPFileReader.getAllRooms();
		_allRobots = BMPFileReader.getAllRobots();
		_shell.setText(WINDOW_NAME);
		_shell.setMaximized(true);

		GridLayout gridLayoutShell = new GridLayout();
		gridLayoutShell.numColumns = 3;
		_shell.setLayout(gridLayoutShell);

		GridLayout gridLayoutleftParentComposite = new GridLayout();
		gridLayoutleftParentComposite.numColumns = 2;
		GridData gridDataLeftParentComposite = new GridData(GridData.CENTER, GridData.BEGINNING, false, true);
		_leftParentComposite.setLayoutData(gridDataLeftParentComposite);
		_leftParentComposite.setLayout(gridLayoutleftParentComposite);

		GridData gridDataRoomCanvas = new GridData(GridData.CENTER, GridData.BEGINNING, false, false);
		gridDataRoomCanvas.horizontalSpan = 2;
		gridDataRoomCanvas.heightHint = 120;
		gridDataRoomCanvas.widthHint = 170;
		GridData gridDataCleaningRobotRoomLabel = new GridData(GridData.CENTER, GridData.BEGINNING, true, true);
		gridDataCleaningRobotRoomLabel.horizontalSpan = 2;
		_roomNameLabel.setText("Change Room");
		_roomNameLabel.setLayoutData(gridDataCleaningRobotRoomLabel);
		_roomLeftArrowButton.setLayoutData(new GridData(GridData.END, GridData.CENTER, true, false));
		_roomtRightArrowButton.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false));

		GridData gridDataRobotCanvas = new GridData(GridData.CENTER, GridData.BEGINNING, false, false);
		gridDataRobotCanvas.horizontalSpan = 2;
		gridDataRobotCanvas.heightHint = 75;
		gridDataRobotCanvas.widthHint = 75;

		_cleaningRobotNameLabel.setText("Change Robot");
		_cleaningRobotNameLabel.setLayoutData(gridDataCleaningRobotRoomLabel);
		_cleaningRobotCanvas.setLayoutData(gridDataRobotCanvas);
		_robotLeftArrowButton.setLayoutData(new GridData(GridData.END, GridData.CENTER, true, false));
		_robotRightArrowButton.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false));

		_robotStartPositionButton.setText("Set start position");
		_robotEndPositionButton.setText("Set end position");

		GridData startPositionButton = new GridData(GridData.END, GridData.CENTER, true, false);
		startPositionButton.widthHint = 100;
		GridData endPositionButton = new GridData(GridData.BEGINNING, GridData.CENTER, true, false);
		endPositionButton.widthHint = 100;
		_robotStartPositionButton.setLayoutData(startPositionButton);
		_robotEndPositionButton.setLayoutData(endPositionButton);

		_label_X.setText("X - Axis");
		_label_Y.setText("Y - Axis");

		GridData resetComboGridData = new GridData(GridData.CENTER, GridData.CENTER, true, false);
		_algorithmnComboBox.setLayoutData(resetComboGridData);
		_algorithmnComboBox.add("Straight Line");
		_algorithmnComboBox.add("sPRM");
		_algorithmnComboBox.add("sPRM Gaussian");
		_algorithmnComboBox.add("RTT");
		_algorithmnComboBox.setEnabled(false);

		_label_sPMRSample.setText("sPMR Sample");
		_label_sPMRRadius.setText("sPMR Radius");
		_runSPMRButtonMultipleTimes.setText("Run sPMR x-times");
		_text_SPMRButtonMultipleTime.setText("10");
		_text_SPMRButtonMultipleTime.setLayoutData(endPositionButton);

		_label_sPMRSuccessRate.setText("sPMR Success Rate:");
		_text_sPMRSuccessRate.setEnabled(false);

		_rrtTimeLabel.setText("RRT Time in ms");
		_rrtRadiusLabel.setText("RRT Radius");

		_resetButton.setText("          Reset all          ");
		resetComboGridData.horizontalSpan = 2;

		_resetButton.setLayoutData(resetComboGridData);

		GridData configurationData = new GridData(GridData.FILL, GridData.CENTER, true, true);
		configurationData.horizontalSpan = 2;

		GridData gridDataTab = new GridData(GridData.FILL, GridData.FILL, false, false);
		gridDataTab.horizontalSpan = 2;
		_tabFolder.setLayoutData(gridDataTab);
		_tabWorkspace.setText("W-Space");
		_tabWorkspace.setControl(_groupWorkspace);
		_tabConfigurationSpace.setText("C-Space");
		_tabConfigurationSpace.setControl(_groupConfigurationSpace);

		_roomMapWorkspace.setBounds(_allRooms.get(_indexOfRoom).getImage().getBounds());
		setConfigurationSpaceSize();

		GridData gridDataSlider = new GridData(GridData.FILL, SWT.FILL, true, false);
		gridDataSlider.horizontalSpan = 2;
		_slider.setLayoutData(gridDataSlider);
		_slider.setEnabled(false);

		setListeners();

	}

	private void setListeners() {

		_roomMapConfigurationSpace.addPaintListener(event -> {
			try {
				if ("Straight Line".equals(_algorithmnComboBox.getItem(_algorithmnComboBox.getSelectionIndex()))) {
					event.gc.drawLine(_straightLine.get(0).getX(), _straightLine.get(0).getY(),
							_straightLine.get(_straightLine.size() - 1).getX(),
							_straightLine.get(_straightLine.size() - 1).getY());
				}

				else if ("sPRM".equals(_algorithmnComboBox.getItem(_algorithmnComboBox.getSelectionIndex()))) {
					
					for(int i = 0; i< SimpleProbabilisticRoadmapMethod._edges.size();i++) {
						Edge e = SimpleProbabilisticRoadmapMethod._edges.get(i);
						event.gc.drawLine(e.getStart().getX(), e.getStart().getY(), e.getEnd().getX(), e.getEnd().getY());
					}
					
					for (int i = 0; i < _sPMRSolution.size() - 1; i++) {
						event.gc.setForeground(_display.getSystemColor(SWT.COLOR_RED));
						event.gc.setLineWidth(3);
						event.gc.drawLine(_sPMRSolution.get(i).getX(), _sPMRSolution.get(i).getY(),
								_sPMRSolution.get(i + 1).getX(), _sPMRSolution.get(i + 1).getY());
						event.gc.setForeground(_display.getSystemColor(SWT.COLOR_BLACK));
						event.gc.setLineWidth(1);
					}
					
					
				}
				else if ("sPRM Gaussian".equals(_algorithmnComboBox.getItem(_algorithmnComboBox.getSelectionIndex()))) {
					
					for(int i = 0; i< SimpleProbabilisticRoadmapMethod._edges.size();i++) {
						Edge e = SimpleProbabilisticRoadmapMethod._edges.get(i);
						event.gc.drawLine(e.getStart().getX(), e.getStart().getY(), e.getEnd().getX(), e.getEnd().getY());
					}
					
					for (int i = 0; i < _sPMRSolution.size() - 1; i++) {
						event.gc.setForeground(_display.getSystemColor(SWT.COLOR_RED));
						event.gc.setLineWidth(3);
						event.gc.drawLine(_sPMRSolution.get(i).getX(), _sPMRSolution.get(i).getY(),
								_sPMRSolution.get(i + 1).getX(), _sPMRSolution.get(i + 1).getY());
						event.gc.setForeground(_display.getSystemColor(SWT.COLOR_BLACK));
						event.gc.setLineWidth(1);
					}
					
				}
				else if ("RTT".equals(_algorithmnComboBox.getItem(_algorithmnComboBox.getSelectionIndex()))) {

					recursion(RapidlyExploringRandomTree.getRoot(), event);
					for (int i = 0; i < _RTTSolution.size() - 1; i++) {
						event.gc.setForeground(_display.getSystemColor(SWT.COLOR_RED));
						event.gc.drawLine(_RTTSolution.get(i).getX(), _RTTSolution.get(i).getY(),
								_RTTSolution.get(i + 1).getX(), _RTTSolution.get(i + 1).getY());
						event.gc.setForeground(_display.getSystemColor(SWT.COLOR_BLACK));
					}

				} else {
				
					event.gc.setForeground(_display.getSystemColor(SWT.COLOR_WHITE));
					event.gc.fillRectangle(	0, 0,
					_allRooms.get(_indexOfRoom).getImage().getBounds().width
							- _allRobots.get(_indexOfRobot).getImage().getBounds().width,
					_allRooms.get(_indexOfRoom).getImage().getBounds().height
							- _allRobots.get(_indexOfRobot).getImage().getBounds().height);
					event.gc.setForeground(_display.getSystemColor(SWT.COLOR_BLACK));
				}

			} catch (Exception t) {
				t.printStackTrace();
				MessageBox messageBox = new MessageBox(_shell, SWT.ICON_ERROR);
				messageBox.setText("Error 198");
				messageBox.setMessage("Cannot compute C-Space! No algorithmn etc. were not set! Error 198");
				messageBox.open();
			}

		});

		setsPMRMultipleTimesButtonListener();
		setComboListener();
		setSliderListener();
		setStartEndButtonListener();
		setResetButtonListener();
		setRoomWorkspacePaintListener();
		setRobotImageListener();
		setArrowListener();
		_roomMapWorkspace.addListener(SWT.MouseUp, event -> {
			_text_X.setText(event.x + "");
			_text_Y.setText(event.y + "");
		});
	}

	private void recursion(TreeNode node, PaintEvent event) {
		if (node.getChildrens() == null) {
			return;
		}
		for (int i = 0; i < node.getChildrens().size(); i++) {
			Point p1 = node.getChildrens().get(i).getData();
			event.gc.drawLine(p1.getX(), p1.getY(), node.getData().getX(), node.getData().getY());
			recursion(node.getChildrens().get(i), event);
		}
	}

	private void setsPMRMultipleTimesButtonListener() {
		_runSPMRButtonMultipleTimes.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {

					int failCount = 0;
					_text_X.setEnabled(false);
					_text_Y.setEnabled(false);
					_text_sPMRRadius.setEnabled(false);
					_text_sPMRSample.setEnabled(false);
					_text_SPMRButtonMultipleTime.setEnabled(false);
					_runSPMRButtonMultipleTimes.setEnabled(false);

					for (int i = 0; i < Integer.parseInt(_text_SPMRButtonMultipleTime.getText()); i++) {
						if (SimpleProbabilisticRoadmapMethod.getSolution(_allRooms.get(_indexOfRoom).get2DPixels(),
								_allRobots.get(_indexOfRobot), new Point(_startXY[0], _startXY[1]),
								new Point(_endXY[0], _endXY[1]), Integer.parseInt(_text_sPMRRadius.getText()),
								Integer.parseInt(_text_sPMRSample.getText())) == null) {
							failCount++;

						}
						double successrate = (failCount / Integer.parseInt(_text_SPMRButtonMultipleTime.getText()))
								* 100;
						_text_sPMRSuccessRate.setText(successrate + "%");
					}
				} catch (Throwable t) {
					t.printStackTrace();
					MessageBox messageBox = new MessageBox(_shell, SWT.ICON_ERROR);
					messageBox.setText("Error 785");
					messageBox.setMessage("Missing sample or radius number! Error 785");
					messageBox.open();
					_slider.setEnabled(false);
				}

				_text_X.setEnabled(true);
				_text_Y.setEnabled(true);
				_text_sPMRRadius.setEnabled(true);
				_text_sPMRSample.setEnabled(true);
				_text_SPMRButtonMultipleTime.setEnabled(true);
				_runSPMRButtonMultipleTimes.setEnabled(true);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// empty

			}
		});
	}

	private void setComboListener() {
		_algorithmnComboBox.addListener(SWT.Selection, event -> {
			_slider.setEnabled(true);

			// add more algorithmn only straigh here
			if ("Straight Line".equals(_algorithmnComboBox.getItem(_algorithmnComboBox.getSelectionIndex()))) {
				_slider.setMinimum(0);
				_straightLine = StraightLine.getStraightWithBresenhamAlgo(_startXY[0], _startXY[1], _endXY[0],
						_endXY[1]);
				_slider.setMaximum(_straightLine.size() + _slider.getThumb() - 1);
			}

			if ("sPRM".equals(_algorithmnComboBox.getItem(_algorithmnComboBox.getSelectionIndex()))) {
				try {
					_slider.setMinimum(0);
					long startTime = System.currentTimeMillis();
					System.out.println("Calculate path with sPRM");
					_sPMRSolution = SimpleProbabilisticRoadmapMethod.getSolution(
							_allRooms.get(_indexOfRoom).get2DPixels(), _allRobots.get(_indexOfRobot),
							new Point(_startXY[0], _startXY[1]), new Point(_endXY[0], _endXY[1]),
							Integer.parseInt(_text_sPMRRadius.getText()), Integer.parseInt(_text_sPMRSample.getText()));
					double measureTime = (double) (System.currentTimeMillis() - startTime)/1000;
					System.out.println("Time needed in seconds = " +  new DecimalFormat("#.##").format(measureTime));
					if(_sPMRSolution == null) {
						_slider.setMaximum(10 + _slider.getThumb() - 1);
					} else {
						_slider.setMaximum(_sPMRSolution.size() + _slider.getThumb() - 1);
					}
					
				} catch (Throwable t) {
					MessageBox messageBox = new MessageBox(_shell, SWT.ICON_ERROR);
					messageBox.setText("Error 205");
					messageBox.setMessage("No solution found for sPMR! More samples or a higher radius needed! Error 205");
					messageBox.open();
					_slider.setEnabled(false);
					resetAll();
				}

			}

			if ("sPRM Gaussian".equals(_algorithmnComboBox.getItem(_algorithmnComboBox.getSelectionIndex()))) {
				try {
					_slider.setMinimum(0);
					
					long startTime = System.currentTimeMillis();
					System.out.println("Calculate path with sPRM_GaussianSampling");
					_sPMRSolution = SimpleProbabilisticRoadmapMethod.getSolutionWithGaussian(
							_allRooms.get(_indexOfRoom).get2DPixels(), _allRobots.get(_indexOfRobot),
							new Point(_startXY[0], _startXY[1]), new Point(_endXY[0], _endXY[1]),
							Integer.parseInt(_text_sPMRRadius.getText()), Integer.parseInt(_text_sPMRSample.getText()));
					double measureTime = (double) (System.currentTimeMillis() - startTime)/1000;
					System.out.println("Time needed in seconds = " +  new DecimalFormat("#.##").format(measureTime));
					if(_sPMRSolution == null) {
						_slider.setMaximum(10 + _slider.getThumb() - 1);
					} else {
						_slider.setMaximum(_sPMRSolution.size() + _slider.getThumb() - 1);
					}
				} catch (Throwable t) {
					System.out.println("No solution found for sPRM");
					MessageBox messageBox = new MessageBox(_shell, SWT.ICON_ERROR);
					messageBox.setText("Error 205");
					messageBox.setMessage("No solution found for sPMR Gaussian! More samples or a higher radius needed! Error 205");
					messageBox.open();
					_slider.setEnabled(false);
					resetAll();
				}

			}

			if ("RTT".equals(_algorithmnComboBox.getItem(_algorithmnComboBox.getSelectionIndex()))) {
				try {
					
					_slider.setMinimum(0);
					long startTime = System.currentTimeMillis();
					System.out.println("Calculate path with RTT");
					_RTTSolution = RapidlyExploringRandomTree.getSolution(_allRooms.get(_indexOfRoom).get2DPixels(),
							_allRobots.get(_indexOfRobot), new Point(_startXY[0], _startXY[1]),
							new Point(_endXY[0], _endXY[1]), Integer.parseInt(_rrtRadiusText.getText()),
							Integer.parseInt(_rrtTimeText.getText()));
					double measureTime = (double) (System.currentTimeMillis() - startTime)/1000;
					System.out.println("Time needed in seconds = " +  new DecimalFormat("#.##").format(measureTime));
					Collections.reverse(_RTTSolution);
					
					_slider.setMaximum(_RTTSolution.size() + _slider.getThumb() - 1);
				} catch (Throwable t) {
					System.out.println("No solution found for RTT");
					MessageBox messageBox = new MessageBox(_shell, SWT.ICON_ERROR);
					messageBox.setText("Error 205");
					messageBox.setMessage("No solution found for RTT! More time or a higher radius needed! Error 205");
					messageBox.open();
					_slider.setEnabled(false);
					_slider.setEnabled(false);
				}

			}

		});
	}

	private void setSliderListener() {
		_slider.addListener(SWT.Selection, event -> {

			// add more algorithmn handle slider drag
			if ("Straight Line".equals(_algorithmnComboBox.getItem(_algorithmnComboBox.getSelectionIndex()))) {

				_roomMapWorkspace.redraw(
						_straightLine.get(_slider.getSelection()).getX()
								- (_allRobots.get(_indexOfRobot).getImage().getBounds().width),
						_straightLine.get(_slider.getSelection()).getY()
								- (_allRobots.get(_indexOfRobot).getImage().getBounds().height),
						_allRobots.get(_indexOfRobot).getImage().getBounds().width * 2,
						_allRobots.get(_indexOfRobot).getImage().getBounds().height * 2, false);
			}
			if ("sPRM".equals(_algorithmnComboBox.getItem(_algorithmnComboBox.getSelectionIndex()))) {
				_roomMapWorkspace.redraw(
						_sPMRSolution.get(_slider.getSelection()).getX()
								- (_allRobots.get(_indexOfRobot).getImage().getBounds().width * 6),
						_sPMRSolution.get(_slider.getSelection()).getY()
								- (_allRobots.get(_indexOfRobot).getImage().getBounds().height * 6),
						_allRobots.get(_indexOfRobot).getImage().getBounds().width * 10,
						_allRobots.get(_indexOfRobot).getImage().getBounds().height * 10, false);
			}
			if ("sPRM Gaussian".equals(_algorithmnComboBox.getItem(_algorithmnComboBox.getSelectionIndex()))) {
				_roomMapWorkspace.redraw(
						_sPMRSolution.get(_slider.getSelection()).getX()
								- (_allRobots.get(_indexOfRobot).getImage().getBounds().width * 6),
						_sPMRSolution.get(_slider.getSelection()).getY()
								- (_allRobots.get(_indexOfRobot).getImage().getBounds().height * 6),
						_allRobots.get(_indexOfRobot).getImage().getBounds().width * 10,
						_allRobots.get(_indexOfRobot).getImage().getBounds().height * 10, false);
			}

			if ("RTT".equals(_algorithmnComboBox.getItem(_algorithmnComboBox.getSelectionIndex()))) {
				_roomMapWorkspace.redraw(

						_RTTSolution.get(_slider.getSelection()).getX()
								- (_allRobots.get(_indexOfRobot).getImage().getBounds().width * 6),
						_RTTSolution.get(_slider.getSelection()).getY()
								- (_allRobots.get(_indexOfRobot).getImage().getBounds().height * 6),
						_allRobots.get(_indexOfRobot).getImage().getBounds().width * 10,
						_allRobots.get(_indexOfRobot).getImage().getBounds().height * 10, false);
			}

		});
	}

	private void setResetButtonListener() {
		_resetButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				resetAll();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// empty

			}
		});
	}

	private void setStartEndButtonListener() {
		_robotStartPositionButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent event) {

				try {
					_startButtonClicked = true;
					_startXY[0] = Integer.parseInt(_text_X.getText());
					_startXY[1] = Integer.parseInt(_text_Y.getText());

					if (CollisionDetection.isInCollision(_allRooms.get(_indexOfRoom).get2DPixels(), _startXY,
							_allRobots.get(_indexOfRobot))) {
						_text_X.setBackground(new Color(_display, 255, 0, 0));
						_text_Y.setBackground(new Color(_display, 255, 0, 0));
					} else {
						_text_X.setBackground(new Color(_display, 0, 255, 0));
						_text_Y.setBackground(new Color(_display, 0, 255, 0));
						_roomMapWorkspace.redraw(
								Integer.parseInt(_text_X.getText())
										- (_allRobots.get(_indexOfRobot).getImage().getBounds().width / 2),
								Integer.parseInt(_text_Y.getText())
										- (_allRobots.get(_indexOfRobot).getImage().getBounds().height / 2),
								_allRobots.get(_indexOfRobot).getImage().getBounds().width,
								_allRobots.get(_indexOfRobot).getImage().getBounds().height, false);
						_robotStartPositionButton.setEnabled(false);
						_isStartPositionSet = true;
						if (_isStartPositionSet && _isEndPositionSet) {
							_algorithmnComboBox.setEnabled(true);
						}
					}
				} catch (Throwable t) {
					MessageBox messageBox = new MessageBox(_shell, SWT.ICON_ERROR);
					messageBox.setText("Error 341");
					messageBox.setMessage("No coordinates found! Error 341");
					messageBox.open();
				}

			}

			public void widgetDefaultSelected(SelectionEvent event) {
				// empty
			}
		});

		_robotEndPositionButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent event) {

				try {

					_endButtonClicked = true;
					_endXY[0] = Integer.parseInt(_text_X.getText());
					_endXY[1] = Integer.parseInt(_text_Y.getText());

					if (CollisionDetection.isInCollision(_allRooms.get(_indexOfRoom).get2DPixels(), _endXY,
							_allRobots.get(_indexOfRobot))) {
						_text_X.setBackground(new Color(_display, 255, 0, 0));
						_text_Y.setBackground(new Color(_display, 255, 0, 0));

					} else {
						_text_X.setBackground(new Color(_display, 0, 255, 0));
						_text_Y.setBackground(new Color(_display, 0, 255, 0));
						_roomMapWorkspace.redraw(
								Integer.parseInt(_text_X.getText())
										- (_allRobots.get(_indexOfRobot).getImage().getBounds().width / 2),
								Integer.parseInt(_text_Y.getText())
										- (_allRobots.get(_indexOfRobot).getImage().getBounds().height / 2),
								_allRobots.get(_indexOfRobot).getImage().getBounds().width,
								_allRobots.get(_indexOfRobot).getImage().getBounds().height, false);
						_robotEndPositionButton.setEnabled(false);
						_isEndPositionSet = true;
						if (_isStartPositionSet && _isEndPositionSet) {
							_algorithmnComboBox.setEnabled(true);
						}
					}
				} catch (Throwable t) {
					MessageBox messageBox = new MessageBox(_shell, SWT.ICON_ERROR);
					messageBox.setText("Error 341");
					messageBox.setMessage("No coordinates found! Error 341");
					messageBox.open();
				}

			}

			public void widgetDefaultSelected(SelectionEvent event) {
				// empty
			}
		});
	}

	private void setRoomWorkspacePaintListener() {
		_roomMapWorkspace.addPaintListener(event -> {

			if (_startButtonClicked) {
				event.gc.drawImage(_allRobots.get(_indexOfRobot).getImage(),
						Integer.parseInt(_text_X.getText())
								- (_allRobots.get(_indexOfRobot).getImage().getBounds().width / 2),
						Integer.parseInt(_text_Y.getText())
								- (_allRobots.get(_indexOfRobot).getImage().getBounds().height / 2));
				_startButtonClicked = false;
			} else if (_endButtonClicked) {
				event.gc.drawImage(_allRobots.get(_indexOfRobot).getImage(),
						Integer.parseInt(_text_X.getText())
								- (_allRobots.get(_indexOfRobot).getImage().getBounds().width / 2),
						Integer.parseInt(_text_Y.getText())
								- (_allRobots.get(_indexOfRobot).getImage().getBounds().height / 2));
				_endButtonClicked = false;

			} else if (_slider.isEnabled() && _slider.getDragDetect()
					&& "Straight Line".equals(_algorithmnComboBox.getItem(_algorithmnComboBox.getSelectionIndex()))) {

				event.gc.drawImage(_allRooms.get(_indexOfRoom).getImage(), 0, 0);

				event.gc.drawImage(_allRobots.get(_indexOfRobot).getImage(),
						_startXY[0] - (_allRobots.get(_indexOfRobot).getImage().getBounds().width / 2),
						_startXY[1] - (_allRobots.get(_indexOfRobot).getImage().getBounds().height / 2));

				event.gc.drawImage(_allRobots.get(_indexOfRobot).getImage(),
						_endXY[0] - (_allRobots.get(_indexOfRobot).getImage().getBounds().width / 2),
						_endXY[1] - (_allRobots.get(_indexOfRobot).getImage().getBounds().height / 2));

				event.gc.drawImage(_allRobots.get(_indexOfRobot).getImage(),
						_straightLine.get(_slider.getSelection()).getX()
								- (_allRobots.get(_indexOfRobot).getImage().getBounds().width / 2),
						_straightLine.get(_slider.getSelection()).getY()
								- (_allRobots.get(_indexOfRobot).getImage().getBounds().height / 2));

			} else if (_slider.isEnabled() && _slider.getDragDetect()
					&& ("sPRM".equals(_algorithmnComboBox.getItem(_algorithmnComboBox.getSelectionIndex()))
							|| "sPRM Gaussian"
									.equals(_algorithmnComboBox.getItem(_algorithmnComboBox.getSelectionIndex())))) {
				event.gc.drawImage(_allRooms.get(_indexOfRoom).getImage(), 0, 0);

				event.gc.drawImage(_allRobots.get(_indexOfRobot).getImage(),
						_startXY[0] - (_allRobots.get(_indexOfRobot).getImage().getBounds().width / 2),
						_startXY[1] - (_allRobots.get(_indexOfRobot).getImage().getBounds().height / 2));

				event.gc.drawImage(_allRobots.get(_indexOfRobot).getImage(),
						_endXY[0] - (_allRobots.get(_indexOfRobot).getImage().getBounds().width / 2),
						_endXY[1] - (_allRobots.get(_indexOfRobot).getImage().getBounds().height / 2));

				if (_sPMRSolution != null) {
					event.gc.drawImage(_allRobots.get(_indexOfRobot).getImage(),
							_sPMRSolution.get(_slider.getSelection()).getX()
									- (_allRobots.get(_indexOfRobot).getImage().getBounds().width / 2),
							_sPMRSolution.get(_slider.getSelection()).getY()
									- (_allRobots.get(_indexOfRobot).getImage().getBounds().height / 2));
				}

			} else if (_slider.isEnabled() && _slider.getDragDetect()
					&& "RTT".equals(_algorithmnComboBox.getItem(_algorithmnComboBox.getSelectionIndex()))) {
				
				
				event.gc.drawImage(_allRooms.get(_indexOfRoom).getImage(), 0, 0);

				event.gc.drawImage(_allRobots.get(_indexOfRobot).getImage(),
						_startXY[0] - (_allRobots.get(_indexOfRobot).getImage().getBounds().width / 2),
						_startXY[1] - (_allRobots.get(_indexOfRobot).getImage().getBounds().height / 2));

				event.gc.drawImage(_allRobots.get(_indexOfRobot).getImage(),
						_endXY[0] - (_allRobots.get(_indexOfRobot).getImage().getBounds().width / 2),
						_endXY[1] - (_allRobots.get(_indexOfRobot).getImage().getBounds().height / 2));

				if (_RTTSolution != null) {
					event.gc.drawImage(_allRobots.get(_indexOfRobot).getImage(),
							_RTTSolution.get(_slider.getSelection()).getX()
									- (_allRobots.get(_indexOfRobot).getImage().getBounds().width / 2),
							_RTTSolution.get(_slider.getSelection()).getY()
									- (_allRobots.get(_indexOfRobot).getImage().getBounds().height / 2));
				}
			}

			else {
				event.gc.drawImage(_allRooms.get(_indexOfRoom).getImage(), 0, 0);
			}

		});
	}

	private void setRobotImageListener() {
		_cleaningRobotCanvas.addPaintListener(event -> {
			event.gc.drawImage(_allRobots.get(_indexOfRobot).getImage(),
					(_cleaningRobotCanvas.getBounds().width
							- _allRobots.get(_indexOfRobot).getImage().getImageData().width) / 2,
					(_cleaningRobotCanvas.getBounds().height
							- _allRobots.get(_indexOfRobot).getImage().getImageData().height) / 2);
		});
	}

	private void setArrowListener() {
		_roomLeftArrowButton.addListener(SWT.Selection, event -> {
			if (_indexOfRoom == 0) {
				_indexOfRoom = _allRooms.size() - 1;
			} else {
				_indexOfRoom--;
			}

			_roomMapWorkspace.redraw();
			resetAll();
		});

		_roomtRightArrowButton.addListener(SWT.Selection, event -> {
			if (_indexOfRoom == _allRooms.size() - 1) {
				_indexOfRoom = 0;
			} else {
				_indexOfRoom++;
			}

			_roomMapWorkspace.redraw();
			resetAll();
		});

		_robotLeftArrowButton.addListener(SWT.Selection, event -> {
			if (_indexOfRobot == 0) {
				_indexOfRobot = _allRobots.size() - 1;
			} else {
				_indexOfRobot--;
			}

			_cleaningRobotCanvas.redraw();
			resetAll();
		});

		_robotRightArrowButton.addListener(SWT.Selection, event -> {
			if (_indexOfRobot == _allRobots.size() - 1) {
				_indexOfRobot = 0;
			} else {
				_indexOfRobot++;
			}

			_cleaningRobotCanvas.redraw();
			resetAll();
		});
	}

	private void resetAll() {
		_text_X.setText("");
		_text_X.setBackground(new Color(_display, 255, 255, 255));
		_text_Y.setBackground(new Color(_display, 255, 255, 255));
		_text_Y.setText("");
		_text_sPMRRadius.setText("");
		_text_sPMRSample.setText("");
		_robotStartPositionButton.setEnabled(true);
		_robotEndPositionButton.setEnabled(true);
		_isStartPositionSet = false;
		_isEndPositionSet = false;
		_algorithmnComboBox.deselectAll();
		_slider.setEnabled(false);
		setConfigurationSpaceSize();
		_roomMapWorkspace.redraw();
		_roomMapConfigurationSpace.redraw();
	}

	private void setConfigurationSpaceSize() {
		_roomMapConfigurationSpace.setBounds(0, 0,
				_allRooms.get(_indexOfRoom).getImage().getBounds().width
						- _allRobots.get(_indexOfRobot).getImage().getBounds().width,
				_allRooms.get(_indexOfRoom).getImage().getBounds().height
						- _allRobots.get(_indexOfRobot).getImage().getBounds().height);
	}

	public static MainWindow getInstance() {
		return MAIN_WINDOW;
	}

	public void run() {
		_shell.open();
		while (!_shell.isDisposed()) {
			if (!_display.readAndDispatch()) {
				_display.sleep();
			}
		}
		_display.dispose();
	}

}
