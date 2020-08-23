package online.abelstrome.retrofitempty;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import android.text.TextUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView txtVwResult, txtVwBanner;
    private Spinner spnSelector;
    private EditText edText;
    private EditText edText2;
    private String gameRefSave = "";
    private String playerNameAndPinSave = "";
    private RetroApi retroApi;
    private Button btnStack;
    //private TableLayout tblLayout;
    private int[][][] btnIDs;
    private int [] currentMoveStep;
    private int areaSize;
    private int areaWidth;
    private int maxAreaSize = 10;
    private int maxAreaWidth = 4;

    private ScrollView sclVwForResults;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtVwResult = findViewById(R.id.txtVwResult3);
        txtVwBanner = findViewById(R.id.txtVwResult2);
        Button btnGo = findViewById(R.id.btnGo);
        edText = findViewById(R.id.editText);
        edText2 = findViewById(R.id.editTextx);
        //tblLayout = findViewById(R.id.tblLayout);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        //int height = displayMetrics.heightPixels;
        int btnWidth = displayMetrics.widthPixels / 12;
        TableRow.LayoutParams btnParams = new TableRow.LayoutParams(btnWidth, btnWidth);
        final TableRow.LayoutParams edTxtParams = new TableRow.LayoutParams();
        btnGo.setLayoutParams(btnParams);

        // Initial set up actions for stacks
        // ---------------------------------
        btnIDs = new int[maxAreaWidth][maxAreaSize][2];
        currentMoveStep = new int[2];
        for (int rowNum = 0; rowNum < maxAreaWidth; rowNum++) {
            for (int colNum = 0; colNum < maxAreaSize; colNum++) {
                String imgViewName = "stack" + rowNum + colNum;
                btnIDs[rowNum][colNum][0] = getResources().getIdentifier(imgViewName, "id", getPackageName());
                btnStack = findViewById(btnIDs[rowNum][colNum][0]);
                //initialise flag that indicates if stack has been selected for a move
                btnIDs[rowNum][colNum][1] = 0;

                btnStack.setLayoutParams(btnParams);
                btnStack.setText("S" + rowNum + colNum);

                // Set click listener for each stack
                // ---------------------------------
                btnStack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (spnSelector.getSelectedItem().toString().equals(getString(R.string.Move))) {
                            int clickedBtn = v.getId();
                            String btnTxt;
                            for (int i = 0; i < 4; i++) {
                                for (int j = 0; j < 10; j++) {
                                    if (clickedBtn == btnIDs[i][j][0]) {
                                        btnStack = findViewById(btnIDs[i][j][0]);
                                        btnTxt = btnStack.getText().toString();
                                        if (btnTxt == "on") btnStack.setText("off");
                                        else btnStack.setText("on");
                                    }
                                }
                            }
                        }
                    }
                });
            }
        }

        //Set options available in spin selector
        spnSelector = findViewById(R.id.spnSelector);
        String[] types = {getString(R.string.PlayerInfo), getString(R.string.GameStatus), getString(R.string.NewGameP1),
                              getString(R.string.NewGameP2), getString(R.string.Move)};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_text, types);
        adapter.setDropDownViewResource(R.layout.spinner_text);
        spnSelector.setAdapter(adapter);

        // Change Spin selector
        // --------------------
        spnSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spnSelector.getSelectedItem().toString().equals(getString(R.string.PlayerInfo))) {
                    edText2.setVisibility(View.INVISIBLE);
                    if (playerNameAndPinSave.length() > 0) edText.setText(playerNameAndPinSave);
                    else edText.setText("");
                    edText.setHint("Enter Player Name");
                    txtVwResult.setText("Player info will be shown here");
                } else if (spnSelector.getSelectedItem().toString().equals(getString(R.string.GameStatus))) {
                    edText2.setVisibility(View.INVISIBLE);
                    if (gameRefSave.length() > 0) edText.setText(gameRefSave);
                    else edText.setText("");
                    edText.setHint("Enter the ref for the game you want to view");
                    txtVwResult.setText("Game status will be shown here");
                } else if (spnSelector.getSelectedItem().toString().equals(getString(R.string.NewGameP1))) {
                    edText2.setVisibility(View.VISIBLE);
                    edText2.setText("");
                    edText2.setHint("Play against GameCentral? Enter Y or N");
                    if (playerNameAndPinSave.length() > 0) edText.setText(playerNameAndPinSave);
                    else edText.setText("");
                    edText.setHint("Enter your player name and pin e.g. fred/1234");
                    txtVwResult.setText("New game status will be shown here");
                } else if (spnSelector.getSelectedItem().toString().equals(getString(R.string.NewGameP2))) {
                    edText2.setVisibility(View.VISIBLE);
                    if (gameRefSave.length() > 0) edText2.setText(gameRefSave);
                    else edText2.setText("");
                    edText2.setHint("Enter the ref of the game you want to join");
                    if (playerNameAndPinSave.length() > 0) edText.setText(playerNameAndPinSave);
                    else edText.setText("");
                    edText.setHint("Enter your player name and pin e.g. fred/1234");
                    txtVwResult.setText("Updated game status will be shown here");
                } else if (spnSelector.getSelectedItem().toString().equals(getString(R.string.Move))) {
                    edText2.setVisibility(View.VISIBLE);
                    if (gameRefSave.length() > 0) edText2.setText(gameRefSave);
                    else edText2.setText("");
                    edText2.setHint("Enter the ref of the game where you want to make a move");
                    if (playerNameAndPinSave.length() > 0) edText.setText(playerNameAndPinSave);
                    else edText.setText("");
                    edText.setHint("Enter your player name and pin e.g. fred/1234");
                    txtVwResult.setText("Updated game status will be shown here");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Click Go button
        // ---------------
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://ec2-18-130-14-91.eu-west-2.compute.amazonaws.com/stacks2/dummy/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                retroApi = retrofit.create(RetroApi.class);

                if (spnSelector.getSelectedItem().toString().equals(getString(R.string.PlayerInfo))) {
                    //use Players api to get information about a player
                    getPlayers();

                } else if (spnSelector.getSelectedItem().toString().equals(getString(R.string.GameStatus))) {
                    //use Games api to get information about a game
                    getGames();

                } else if (spnSelector.getSelectedItem().toString().equals(getString(R.string.NewGameP1))) {
                    //use New Game api to start a new game
                    newP1Game();

                } else if (spnSelector.getSelectedItem().toString().equals(getString(R.string.NewGameP2))) {
                    //use api to add P2 to an existing game
                    addNewP2ToGame();

                } else if (spnSelector.getSelectedItem().toString().equals(getString(R.string.Move))) {
                    //use api to move a move
                    makeMove();
                }
            }

            //Make a move
            //-----------
            private void makeMove() {
                String playerNameAndPin = edText.getText().toString();
                String gameRef = edText2.getText().toString();
                String response = validatePlayerNameAndPin(playerNameAndPin);
                if (response.equals("")) {
                    //if playerName is empty display message
                    txtVwResult.setText(response);
                } else if (!TextUtils.isDigitsOnly(gameRef) | gameRef.isEmpty()) {
                    //game ref is not numeric
                    txtVwResult.setText(R.string.GameRefMissing);
                } else {
                    //all inputs are valid

                    //save inputs
                    playerNameAndPinSave = playerNameAndPin;
                    gameRefSave = gameRef;

                    //*******************************************************
                    //*** add code to capture the move position and direction
                    //*******************************************************
                    String pin = playerNameAndPin.substring(playerNameAndPin.indexOf("/") + 1);
                    String name = playerNameAndPin.substring(0, playerNameAndPin.indexOf("/"));

                    GamesMakeMove moveGame = new GamesMakeMove(pin,3,800);

                    Call<GamesMakeMove> call = retroApi.makeMove(gameRef, name, moveGame);

                    call.enqueue(new Callback<GamesMakeMove>() {
                        @Override
                        public void onResponse(Call<GamesMakeMove> call, Response<GamesMakeMove> response) {
                            if (!response.isSuccessful()) {
                                String errMsg = "Response code: " + response.code();
                                txtVwResult.setText(errMsg);
                                return;
                            }

                            GamesMakeMove gamesMsgBody = response.body();
                            Log.d("xxxxxxxxresponse: ", "API response message = " + gamesMsgBody);
                            String content = "";
                            assert gamesMsgBody != null;
                            if (gamesMsgBody.getErrorCode() == null) {
                                //List<GamesStacks> stacks = gamesMsgBody.getGamesStacks();
                                areaSize = Integer.parseInt(gamesMsgBody.getAreaSize());
                                areaWidth = Integer.parseInt(gamesMsgBody.getAreaWidth());
                                //content = formatGamesMakeMoveInfo(gamesMsgBody);
                                content = formatGamesData(gamesMsgBody.getGameRef(),
                                        gamesMsgBody.getAreaSize(), gamesMsgBody.getAreaWidth(),
                                        gamesMsgBody.getPlayer1Name(), gamesMsgBody.getPlayer2Name(),
                                        gamesMsgBody.getTurnCount(), gamesMsgBody.getLastPlayer());

                                // Make rows and stacks outside the playing area invisible
                                makePlayingAreaVisible(areaWidth,areaSize);

                                // Label stacks
                                //labelStacks3(gamesMsgBody,areaWidth,areaSize);
                                labelStacks1(gamesMsgBody.getGamesStacks(), areaWidth, areaSize);

                                // Allow user to make a move


                            } else {
                                content += "error code: " + gamesMsgBody.getErrorCode() + "\n";
                                content += "error message: " + gamesMsgBody.getErrorMsg() + "\n";

                                // make all rows invisible
                                makeAllRowsInvisible();
                            }
                            //Log.d("xxxxxxxxresponse: ", "content" + content);
                            txtVwResult.setText(content);
                        }

                        @Override
                        public void onFailure(Call<GamesMakeMove> call, Throwable t) {
                            txtVwResult.setText(t.getMessage());
                        }
                    });
                }
            }

            // Add Player 2 to new game
            // ------------------------
            private void addNewP2ToGame() {

                final String playerNameAndPin = edText.getText().toString();
                String gameRef =edText2.getText().toString();
                String response = validatePlayerNameAndPin(playerNameAndPin);

                if (!response.equals("")) {
                    txtVwResult.setText(response);
                }  else if (!TextUtils.isDigitsOnly(gameRef) | gameRef.isEmpty()) {
                    //game ref is not numeric
                    txtVwResult.setText(R.string.GameRefMissing);
                } else {
                    //all inputs are valid - ready to make call API

                    //save inputs
                    playerNameAndPinSave = playerNameAndPin;
                    gameRefSave = gameRef;

                    String pin = playerNameAndPin.substring(playerNameAndPin.indexOf("/") + 1);
                    String name = playerNameAndPin.substring(0, playerNameAndPin.indexOf("/"));

                    GamesAddP2 addP2Game = new GamesAddP2(name, pin);

                    Call<GamesAddP2> call = retroApi.addNewP2ToGame(gameRef, addP2Game);

                    call.enqueue(new Callback<GamesAddP2>() {
                        @Override
                        public void onResponse(Call<GamesAddP2> call, Response<GamesAddP2> response) {
                            if (!response.isSuccessful()) {
                                String errMsg = "Response code: " + response.code();
                                txtVwResult.setText(errMsg);
                                return;
                            }

                            GamesAddP2 gamesMsgBody = response.body();
                            Log.d("xxxxxxxxresponse: ", "API response message = " + gamesMsgBody);
                            String content = "";
                            assert gamesMsgBody != null;
                            if (gamesMsgBody.getErrorCode() == null) {
                                //List<GamesStacks> stacks = gamesMsgBody.getGamesStacks();
                                areaSize = Integer.parseInt(gamesMsgBody.getAreaSize());
                                areaWidth = Integer.parseInt(gamesMsgBody.getAreaWidth());
                                //content = formatGamesAddP2Info(gamesMsgBody);
                                content = formatGamesData(gamesMsgBody.getGameRef(),
                                        gamesMsgBody.getAreaSize(), gamesMsgBody.getAreaWidth(),
                                        gamesMsgBody.getPlayer1Name(), gamesMsgBody.getPlayer2Name(),
                                        gamesMsgBody.getTurnCount(), gamesMsgBody.getLastPlayer());


                                // Make rows and stacks outside the playing area invisible
                                makePlayingAreaVisible(areaWidth,areaSize);

                                // Label stacks
                                //labelStacks2(gamesMsgBody,areaWidth,areaSize);
                                labelStacks1(gamesMsgBody.getGamesStacks(), areaWidth, areaSize);

                                // Allow user to make a move
                                

                            } else {
                                content += "error code: " + gamesMsgBody.getErrorCode() + "\n";
                                content += "error message: " + gamesMsgBody.getErrorMsg() + "\n";

                                // make all rows invisible
                                makeAllRowsInvisible();
                            }
                            //Log.d("xxxxxxxxresponse: ", "content" + content);
                            txtVwResult.setText(content);
                        }

                        @Override
                        public void onFailure(Call<GamesAddP2> call, Throwable t) {
                            txtVwResult.setText(t.getMessage());
                        }
                    });
                }
            }

            // New game with P1 player
            // -----------------------
            private void newP1Game() {

                final String playerNameAndPin = edText.getText().toString();
                String response = validatePlayerNameAndPin(playerNameAndPin);

                if (!response.equals("")) {
                    //if playerName is empty display message
                    txtVwResult.setText(response);
                } else if (!edText2.getText().toString().toUpperCase().equals("N")
                        & !edText2.getText().toString().toUpperCase().equals("Y")
                        & !edText2.getText().toString().isEmpty()) {
                    // GameCentral flag not "Y" or "N"
                    txtVwResult.setText(R.string.NotYorN);
                } else {

                    //all inputs are valid

                    //save inputs
                    playerNameAndPinSave = playerNameAndPin;

                    String pin = playerNameAndPin.substring(playerNameAndPin.indexOf("/") + 1);
                    String name = playerNameAndPin.substring(0, playerNameAndPin.indexOf("/"));
                    String withGC;
                    if (edText2.getText().toString().toUpperCase().equals("Y")) withGC = "Y";
                    else withGC = "N";
                    Games newP1Game = new Games(name, pin, withGC);
                    Call<Games> call = retroApi.createNewP1Game(newP1Game);

                    call.enqueue(new Callback<Games>() {
                        @Override
                        public void onResponse(Call<Games> call, Response<Games> response) {
                            if (!response.isSuccessful()) {
                                String errMsg = "Response code: " + response.code();
                                txtVwResult.setText(errMsg);
                                return;
                            }

                            Games gamesMsgBody = response.body();
                            Log.d("xxxxxxxxresponse: ", "API response message = " + gamesMsgBody);
                            String content = "";
                            assert gamesMsgBody != null;
                            if (gamesMsgBody.getErrorCode() == null) {
                                //List<GamesStacks> stacks = gamesMsgBody.getGamesStacks();
                                areaSize = Integer.parseInt(gamesMsgBody.getAreaSize());
                                areaWidth = Integer.parseInt(gamesMsgBody.getAreaWidth());
                                //content = formatGameInfo(gamesMsgBody);
                                content = formatGamesData(gamesMsgBody.getGameRef(),
                                                        gamesMsgBody.getAreaSize(), gamesMsgBody.getAreaWidth(),
                                                        gamesMsgBody.getPlayer1Name(), gamesMsgBody.getPlayer2Name(),
                                                        gamesMsgBody.getTurnCount(), gamesMsgBody.getLastPlayer());

                                // Make rows and stacks outside the playing area invisible
                                makePlayingAreaVisible(areaWidth,areaSize);

                                // label stacks
                                labelStacks1(gamesMsgBody.getGamesStacks(), areaWidth, areaSize);

                                //save gameRef
                                gameRefSave = gamesMsgBody.getGameRef();

                            } else {
                                content += "error code: " + gamesMsgBody.getErrorCode() + "\n";
                                content += "error message: " + gamesMsgBody.getErrorMsg() + "\n";

                                // make all rows invisible
                                makeAllRowsInvisible();
                            }
                            //Log.d("xxxxxxxxresponse: ", "content" + content);
                            txtVwResult.setText(content);

                        }

                        @Override
                        public void onFailure(Call<Games> call, Throwable t) {
                            txtVwResult.setText(t.getMessage());
                        }
                    });

                }
            }

            // Get game info
            // -------------
            private void getGames() {
                String gameRef = edText.getText().toString();

            if (!TextUtils.isDigitsOnly(gameRef) | gameRef.isEmpty()) {
                //game ref is not numeric
                txtVwResult.setText(R.string.GameRefMissing);
            }
            else {
                    //inputs are valid

                    //save inputs
                    gameRefSave = gameRef;

                    Call<Games> call = retroApi.getGames(gameRef);

                    call.enqueue(new Callback<Games>() {
                        @Override
                        public void onResponse(Call<Games> call, Response<Games> response) {
                            if (!response.isSuccessful()) {
                                String errMsg = "Response code: " + response.code();
                                txtVwResult.setText(errMsg);
                                return;
                            }

                            Games gamesMsgBody = response.body();

                            String content = "";

                            assert gamesMsgBody != null;
                            if (gamesMsgBody.getErrorCode() == null) {
                                //List<GamesStacks> stacks = gamesMsgBody.getGamesStacks();
                                areaSize = Integer.parseInt(gamesMsgBody.getAreaSize());
                                areaWidth = Integer.parseInt(gamesMsgBody.getAreaWidth());
                                content = formatGameInfo(gamesMsgBody);
/*
                                content = formatGamesData(gamesMsgBody.getGameRef(),
                                        gamesMsgBody.getAreaSize(), gamesMsgBody.getAreaWidth(),
                                        gamesMsgBody.getPlayer1Name(), gamesMsgBody.getPlayer2Name(),
                                        gamesMsgBody.getTurnCount(), gamesMsgBody.getLastPlayer());
*/

                                // Make rows and stacks outside the playing area invisible
                                makePlayingAreaVisible(areaWidth, areaSize);

                                // label stacks
                                labelStacks1(gamesMsgBody.getGamesStacks(), areaWidth, areaSize);

                            } else {
                                content += "error code: " + gamesMsgBody.getErrorCode() + "\n";
                                content += "error message: " + gamesMsgBody.getErrorMsg() + "\n";

                                // make all rows invisible
                                makeAllRowsInvisible();
                            }

                            txtVwResult.setText(content);
                        }

                        @Override
                        public void onFailure(Call<Games> call, Throwable t) {
                            txtVwResult.setText(t.getMessage());
                        }
                    });

                }
            }

            // Get player info
            // ---------------
            private void getPlayers() {

                // make all rows invisible
                makeAllRowsInvisible();

                final String playerName = edText.getText().toString();

                if (playerName.equals("")) {
                    //if playerName is empty display message
                    txtVwResult.setText(R.string.PlayerNameMissing);
                }
                else if (!playerName.equals(playerName.replaceAll("[^a-zA-Z0-9]", ""))) {
                    //if player name contains spaces and/or special characters
                    txtVwResult.setText("Player name must be letters and numbers only");
                }
                else {

                    //all inputs are valid

                    //save inputs
                    playerNameAndPinSave = playerName;

                    Call<Players> call = retroApi.getPlayers(playerName);

                    call.enqueue(new Callback<Players>() {
                        @Override
                        public void onResponse(Call<Players> call, Response<Players> response) {
                            if (!response.isSuccessful()) {
                                String errMsg = "Response code: " + response.code();
                                txtVwResult.setText(errMsg);
                                return;
                            }

                            Players playersMsgBody = response.body();

                            String content = "";
                            assert playersMsgBody != null;
                            if (playersMsgBody.getErrorCode() == null) {
                                content += "type: " + playersMsgBody.getType() + "   ";
                                content += "player name: " + playersMsgBody.getPlayerName() + "\n";
                                List<PlayersGames> games = playersMsgBody.getPlayersGames();
                                content += "is associated with: " + games.size() + " games\n";
                                if (games.size() > 0)
                                    content += "game 1 ref: " + playersMsgBody.getPlayersGames().get(0).getGameRef() + "   ";
                                if (games.size() > 1)
                                    content += "game 2 status: " + playersMsgBody.getPlayersGames().get(1).getGameState() + "\n";
                            } else {
                                content += "error code: " + playersMsgBody.getErrorCode() + "\n";
                                content += "error message: " + playersMsgBody.getErrorMsg() + "\n";
                            }

                            txtVwResult.setText(content);
                        }

                        @Override
                        public void onFailure(Call<Players> call, Throwable t) {
                            txtVwResult.setText(t.getMessage());
                        }

                    });
                }
            }

            // Format game info into a string
            // ------------------------------

            private String formatGameInfo(Games gamesMsgBody) {
                String content = "";
                content += "Game ref: " + gamesMsgBody.getGameRef() + "   " + "area size: " + gamesMsgBody.getAreaSize() + "   ";
                content += "area width: " + gamesMsgBody.getAreaWidth() + "   " + "turn count: " + gamesMsgBody.getTurnCount() + "\n";
                if (gamesMsgBody.getPlayer1Name().isEmpty()) content += "player 1: " + "--" + "   ";
                else content += "player 1: " + gamesMsgBody.getPlayer1Name() + "   ";
                if (gamesMsgBody.getPlayer2Name().isEmpty()) content += "player 2: " + "--" + "   ";
                else content += "player 2: " + gamesMsgBody.getPlayer2Name() + "   ";
                if (gamesMsgBody.getLastPlayer().isEmpty()) content += "last player: " + "--" + "   ";
                else content += "last player: " + gamesMsgBody.getLastPlayer() + "\n";
                return content;
            }
/*
            private String formatGamesAddP2Info(GamesAddP2 gamesMsgBody) {
                String content = "";
                content += "Game ref: " + gamesMsgBody.getGameRef() + "   " + "area size: " + gamesMsgBody.getAreaSize() + "   ";
                content += "area width: " + gamesMsgBody.getAreaWidth() + "   " + "turn count: " + gamesMsgBody.getTurnCount() + "\n";
                if (gamesMsgBody.getPlayer1Name().isEmpty()) content += "player 1: " + "--" + "   ";
                else content += "player 1: " + gamesMsgBody.getPlayer1Name() + "   ";
                if (gamesMsgBody.getPlayer2Name().isEmpty()) content += "player 2: " + "--" + "   ";
                else content += "player 2: " + gamesMsgBody.getPlayer2Name() + "   ";
                if (gamesMsgBody.getLastPlayer().isEmpty()) content += "last player: " + "--" + "   ";
                else content += "last player: " + gamesMsgBody.getLastPlayer() + "\n";
                return content;
            }
            private String formatGamesMakeMoveInfo(GamesMakeMove gamesMsgBody) {
                String content = "";
                content += "Game ref: " + gamesMsgBody.getGameRef() + "   " + "area size: " + gamesMsgBody.getAreaSize() + "   ";
                content += "area width: " + gamesMsgBody.getAreaWidth() + "   " + "turn count: " + gamesMsgBody.getTurnCount() + "\n";
                if (gamesMsgBody.getPlayer1Name().isEmpty()) content += "player 1: " + "--" + "   ";
                else content += "player 1: " + gamesMsgBody.getPlayer1Name() + "   ";
                if (gamesMsgBody.getPlayer2Name().isEmpty()) content += "player 2: " + "--" + "   ";
                else content += "player 2: " + gamesMsgBody.getPlayer2Name() + "   ";
                if (gamesMsgBody.getLastPlayer().isEmpty()) content += "last player: " + "--" + "   ";
                else content += "last player: " + gamesMsgBody.getLastPlayer() + "\n";
                return content;
            }
*/
            private String formatGamesData(String gameRef, String areaSize, String areaWidth,
                                           String player1Name, String player2Name, String turnCount, String lastPlayer) {
                String content = "";
                content += "Game ref: " + gameRef + "   " + "area size: " + areaSize + "   ";
                content += "area width: " + areaWidth + "   " + "turn count: " + turnCount + "\n";
                if (player1Name.isEmpty()) content += "player 1: " + "--" + "   ";
                else content += "player 1: " + player1Name + "   ";
                if (player2Name.isEmpty()) content += "player 2: " + "--" + "   ";
                else content += "player 2: " + player2Name+ "   ";
                if (lastPlayer.isEmpty()) content += "last player: " + "--" + "   ";
                else content += "last player: " + lastPlayer + "\n";
                return content;
            }


            // make all rows invisible
            // -----------------------
            private void makeAllRowsInvisible() {
                int tblRowID;
                 TableRow tblRow;
                //for (int rowNum = 0; rowNum < 4; rowNum++) {
                for (int rowNum = 0; rowNum < maxAreaWidth; rowNum++) {
                    String imgViewName = "tblRow" + rowNum;
                    tblRowID = getResources().getIdentifier(imgViewName, "id", getPackageName());
                    tblRow = findViewById(tblRowID);
                    tblRow.setVisibility(View.INVISIBLE);
                }
            }

            // label stacks
            // ------------
/*
            private void labelStacks(Games gamesMsgBody, int areaWidthIn, int areaSizeIn) {
                int index;
                Drawable circle_E, circle_5, circle_4;
                circle_4 = getResources().getDrawable(R.drawable.stack_circle4);
                circle_5 = getResources().getDrawable(R.drawable.stack_circle5);
                circle_E = getResources().getDrawable(R.drawable.stack_circle_empty);
                String messageText;
                for (int rowNum = 0; rowNum < areaWidthIn; rowNum++) {
                    for (int colNum = 0; colNum < areaSizeIn + 2; colNum++) {
                        index = (rowNum * (areaSizeIn + 2)) + colNum;
                        btnStack = findViewById(btnIDs[areaWidthIn - rowNum - 1][colNum][0]);
                        if (gamesMsgBody.getGamesStacks().get(index).getTop().length() == 0) {
                            //stack is empty
                            btnStack.setText("");
                            btnStack.setBackground(circle_E);
                        } else {
                            messageText = gamesMsgBody.getGamesStacks().get(index).getTop() + "-"
                                    + gamesMsgBody.getGamesStacks().get(index).getHeight();
                            btnStack.setText(messageText);
                            if (gamesMsgBody.getGamesStacks().get(index).getTop().equals("P1")) btnStack.setBackground(circle_4);
                            else btnStack.setBackground(circle_5);
                        }
                    }
                }
            }
            private void labelStacks2(GamesAddP2 gamesMsgBody, int areaWidthIn, int areaSizeIn) {
                int index;
                Drawable circle_E, circle_5, circle_4;
                circle_4 = getResources().getDrawable(R.drawable.stack_circle4);
                circle_5 = getResources().getDrawable(R.drawable.stack_circle5);
                circle_E = getResources().getDrawable(R.drawable.stack_circle_empty);
                String messageText;
                for (int rowNum = 0; rowNum < areaWidthIn; rowNum++) {
                    for (int colNum = 0; colNum < areaSizeIn + 2; colNum++) {
                        index = (rowNum * (areaSizeIn + 2)) + colNum;
                        btnStack = findViewById(btnIDs[areaWidthIn - rowNum - 1][colNum][0]);
                        if (gamesMsgBody.getGamesStacks().get(index).getTop().length() == 0) {
                            //stack is empty
                            btnStack.setText("");
                            btnStack.setBackground(circle_E);
                        } else {
                            messageText = gamesMsgBody.getGamesStacks().get(index).getTop() + "-"
                                    + gamesMsgBody.getGamesStacks().get(index).getHeight();
                            btnStack.setText(messageText);
                            if (gamesMsgBody.getGamesStacks().get(index).getTop().equals("P1")) btnStack.setBackground(circle_4);
                            else btnStack.setBackground(circle_5);
                        }
                    }
                }
            }
            private void labelStacks3(GamesMakeMove gamesMsgBody, int areaWidthIn, int areaSizeIn) {
                int index;
                Drawable circle_E, circle_5, circle_4;
                circle_4 = getResources().getDrawable(R.drawable.stack_circle4);
                circle_5 = getResources().getDrawable(R.drawable.stack_circle5);
                circle_E = getResources().getDrawable(R.drawable.stack_circle_empty);
                String messageText;
                for (int rowNum = 0; rowNum < areaWidthIn; rowNum++) {
                    for (int colNum = 0; colNum < areaSizeIn + 2; colNum++) {
                        index = (rowNum * (areaSizeIn + 2)) + colNum;
                        btnStack = findViewById(btnIDs[areaWidthIn - rowNum - 1][colNum][0]);
                        if (gamesMsgBody.getGamesStacks().get(index).getTop().length() == 0) {
                            //stack is empty
                            btnStack.setText("");
                            btnStack.setBackground(circle_E);
                        } else {
                            messageText = gamesMsgBody.getGamesStacks().get(index).getTop() + "-"
                                    + gamesMsgBody.getGamesStacks().get(index).getHeight();
                            btnStack.setText(messageText);
                            if (gamesMsgBody.getGamesStacks().get(index).getTop().equals("P1")) btnStack.setBackground(circle_4);
                            else btnStack.setBackground(circle_5);
                        }
                    }
                }
            }
*/
            private void labelStacks1(List<GamesStacks> gamesStacks, int areaWidthIn, int areaSizeIn) {
                int index;
                Drawable circle_E, circle_5, circle_4, arrowUp, arrowDown, arrowLeft,arrowRight;
                circle_4 = getResources().getDrawable(R.drawable.stack_circle4);
                circle_5 = getResources().getDrawable(R.drawable.stack_circle5);
                arrowUp = getResources().getDrawable(R.drawable.arrow_up);
                arrowDown = getResources().getDrawable(R.drawable.arrow_down);
                circle_E = getResources().getDrawable(R.drawable.stack_circle_empty);

                String messageText;
                for (int rowNum = 0; rowNum < areaWidthIn; rowNum++) {
                    for (int colNum = 0; colNum < areaSizeIn + 2; colNum++) {
                        index = (rowNum * (areaSizeIn + 2)) + colNum;
                        btnStack = findViewById(btnIDs[areaWidthIn - rowNum - 1][colNum][0]);
                        if (gamesStacks.get(index).getTop().length() == 0) {
                            //stack is empty
                            btnStack.setText("");
                            btnStack.setBackground(circle_E);
                        } else {
                            messageText = gamesStacks.get(index).getTop() + "-"
                                    + gamesStacks.get(index).getHeight();
                            btnStack.setText(messageText);
                            if (gamesStacks.get(index).getTop().equals("P1")) btnStack.setBackground(circle_4);
                            else btnStack.setBackground(circle_5);
                        }
                    }
                }
            }

            // Make rows and stacks outside the playing area invisible
            // -------------------------------------------------------
            private void makePlayingAreaVisible(int areaWidthIn, int areaSizeIn){

                // make rows outside play area invisible
                int tblRowID;
                TableRow tblRow;
                String imgViewName;
                for (int rowNum = 0; rowNum < maxAreaWidth; rowNum++) {
                    imgViewName = "tblRow" + rowNum;
                    tblRowID = getResources().getIdentifier(imgViewName, "id", getPackageName());
                    tblRow = findViewById(tblRowID);
                    if (rowNum < areaWidth) {
                        tblRow.setVisibility(View.VISIBLE);
                    } else {
                        tblRow.setVisibility(View.INVISIBLE);
                    }
                }

                // make stacks outside playing area invisible
                for (int rowNum = 0; rowNum < areaWidthIn; rowNum++) {
                    for (int colNum = areaSizeIn + 2; colNum < maxAreaSize; colNum++) {
                        btnStack = findViewById(btnIDs[rowNum][colNum][0]);
                        if (colNum < areaSizeIn + 2) {
                            btnStack.setVisibility(View.VISIBLE);
                        } else {
                            btnStack.setVisibility(View.INVISIBLE);
                        }
                    }
                }

            }

            //Validate input player name and pin
            //----------------------------------
            private String validatePlayerNameAndPin(String inputPlayerName) {
                String returnResp = "";
                if (inputPlayerName.equals("")) {
                    //if playerName is empty display message
                    returnResp = getString(R.string.PlayerNamePinMissing);
                } else if (inputPlayerName.indexOf("/") == 0) {
                    //backslash cannot be first character
                    returnResp = getString(R.string.PlayerNamePinMissing);
                } else if (!inputPlayerName.contains("/")) {
                    //if playerName doesn't include a backslash for the pin number
                    returnResp = getString(R.string.PlayerNamePinMissing);
                } else if (inputPlayerName.indexOf("/") == inputPlayerName.length() - 1) {
                    //if  backslash is the last character
                    returnResp =  getString(R.string.PlayerNamePinMissing);
                }
                if (returnResp.equals("")) {
                    String pin = inputPlayerName.substring(inputPlayerName.indexOf("/") + 1);
                    String name = inputPlayerName.substring(0, inputPlayerName.indexOf("/"));
                    if (!TextUtils.isDigitsOnly(pin)) {
                        //if pin is not all digits
                        returnResp = getString(R.string.PlayerNamePinMissing);
                    } else if (!name.equals(name.replaceAll("[^a-zA-Z0-9]", ""))) {
                        //if player name contains special characters or a space
                        returnResp = getString(R.string.PlayerNamePinMissing);
                    }
                }
                return returnResp;
            }

        });
    }
}
