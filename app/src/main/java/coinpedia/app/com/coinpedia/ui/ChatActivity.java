package coinpedia.app.com.coinpedia.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import coinpedia.app.com.coinpedia.R;
import coinpedia.app.com.coinpedia.chat.ChatListAdapter;
import coinpedia.app.com.coinpedia.chat.chatAdapter.ChatInit;
import coinpedia.app.com.coinpedia.chat.chatAdapter.ChatInitModel;
import coinpedia.app.com.coinpedia.chat.chatAdapter.ChatOptions;
import coinpedia.app.com.coinpedia.chat.chatAdapter.ChatOptionsModel;
import coinpedia.app.com.coinpedia.chat.model.ChatMessage;
import coinpedia.app.com.coinpedia.chat.model.Status;
import coinpedia.app.com.coinpedia.chat.model.UserType;
import coinpedia.app.com.coinpedia.font.FontChangeCrawler;
import coinpedia.app.com.coinpedia.parseApi.ApiClient;
import coinpedia.app.com.coinpedia.parseApi.ApiInterface;
import coinpedia.app.com.coinpedia.utils.ConnectivityManagerClass;
import coinpedia.app.com.coinpedia.utils.Consts;
import coinpedia.app.com.coinpedia.utils.Navigatior;
import coinpedia.app.com.coinpedia.utils.SharedPrefClass;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChatActivity extends BaseActivityWithBack implements AdapterView.OnItemClickListener{

    private static final String TAG = ChatActivity.class.getSimpleName().toString();
    private ListView chatListView;
    private EditText chatEditText1;
    private ArrayList<ChatMessage> chatMessages;
    private ImageView enterChatView1;
    private ChatListAdapter listAdapter;
    String mainUrl = Consts.chatUrl;

    private EditText.OnKeyListener keyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                EditText editText = (EditText) v;
                if(v==chatEditText1)
                {
                    if(chatEditText1.getText().toString().length() > 0)
                        funcConversationMessage(chatEditText1.getText().toString());
                }
                chatEditText1.setText("");
                return true;
            }
            return false;
        }
    };

    private ImageView.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v==enterChatView1)
            {   //sendMessage(chatEditText1.getText().toString(), UserType.OTHER);
                if(chatEditText1.getText().toString().length() > 0)
                    funcConversationMessage(chatEditText1.getText().toString());
            }
            chatEditText1.setText("");
        }
    };

    private final TextWatcher watcher1 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (chatEditText1.getText().toString().equals("")) {
            } else {
                enterChatView1.setImageResource(R.drawable.ic_chat_send);
            }
        }
        @Override
        public void afterTextChanged(Editable editable) {
            if(editable.length()==0){
                enterChatView1.setImageResource(R.drawable.ic_chat_send);
            }else{
                enterChatView1.setImageResource(R.drawable.ic_chat_send_active);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        chatMessages = new ArrayList<>();
        chatListView = (ListView) findViewById(R.id.chat_list_view);
        chatEditText1 = (EditText) findViewById(R.id.chat_edit_text1);
        enterChatView1 = (ImageView) findViewById(R.id.enter_chat1);

        listAdapter = new ChatListAdapter(chatMessages, this);
        chatListView.setAdapter(listAdapter);
        chatListView.setOnItemClickListener(this);
        chatEditText1.setOnKeyListener(keyListener);
        enterChatView1.setOnClickListener(clickListener);
        chatEditText1.addTextChangedListener(watcher1);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), Consts.font3,Consts.font2);
        fontChanger.replaceFonts((ViewGroup) findViewById(android.R.id.content));

        if(ConnectivityManagerClass.getInstance().isNetworkAvailable(ChatActivity.this) == true) {
            if (new SharedPrefClass(ChatActivity.this).getChatInit() == false) {
                funcInitMessage();
            }
        }else {
            new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                    .setTitleText("Oops!")
                    .setContentText("Network Failed! Check your Internet")
                    .setCustomImage(R.drawable.ic_disconnected)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            Navigatior.getClassInstance().navigateToActivity(ChatActivity.this, HomeActivity.class);
                        }
                    })
                    .show();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //      Toast.makeText(getApplicationContext(),"Back button clicked", Toast.LENGTH_SHORT).show();
                Navigatior.getClassInstance().navigateToActivity(ChatActivity.this, HomeActivity.class);
                break;
        }
        return true;
    }
    public void funcInitMessage() {

        ChatInit chatInit = new ChatInit();
        chatInit.method= "initChat";

        ApiInterface apiInterface = ApiClient.getClient(mainUrl).create(ApiInterface.class);
        apiInterface.getChatInitResponse(chatInit).enqueue(new Callback<ChatInitModel>() {
            @Override
            public void onResponse(Call<ChatInitModel> call, Response<ChatInitModel> response) {
                if(response.isSuccessful())
                {
                    ChatInitModel chatModel = response.body();
                    if(chatModel != null)
                    {
                        String initPhrase = chatModel.getResult();
                        funcReceivedMessage(1, initPhrase, 1);
                        List<String> initOption = chatModel.getInitialOptions();
                        for(int i = 0; i<initOption.size(); i++)
                        {   String optionWord = initOption.get(i).toString();
                            funcReceivedMessage(1, optionWord, 2);   }
                    }

                }else {
               //     Toast.makeText(ChatActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ChatInitModel> call, Throwable t) {
          //      Toast.makeText(ChatActivity.this, "Failed", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void funcConversationMessage(String messageText)
    {
        ChatOptions chatOptions = new ChatOptions();
        chatOptions.method= "sendMessage";
        chatOptions.user_message = messageText;
        funcSendMessage(messageText);

        ApiInterface apiInterface = ApiClient.getClient(mainUrl).create(ApiInterface.class);
        apiInterface.getChatOptionsResponse(chatOptions).enqueue(new Callback<ChatOptionsModel>() {
            @Override
            public void onResponse(Call<ChatOptionsModel> call, Response<ChatOptionsModel> response) {
                if(response.isSuccessful())
                {
                    try {
                        ChatOptionsModel chatOptionsModel = response.body();
                        if (chatOptionsModel != null) {
                            String resultPhrase = chatOptionsModel.getResult();
                            funcReceivedMessage(1, resultPhrase, 1);
                            List<String> responseOptions = chatOptionsModel.getOptions();
                            for (int i = 0; i < responseOptions.size(); i++) {
                                String optionWord = responseOptions.get(i).toString();
                                funcReceivedMessage(1, optionWord, 2);
                            }
                        }
                    }catch (NullPointerException npe)
                    {
                        Log.e(TAG, "Err : "+npe, npe);
                    }
                }else {
            //        Toast.makeText(ChatActivity.this, "Something went wrong-Options", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ChatOptionsModel> call, Throwable t) {
            }
        });
    }

    private void funcReceivedMessage(int status, String messageText, int botStatus)
    {
        if(status == 1)
        {
            final ChatMessage message = new ChatMessage();
            message.setMessageStatus(Status.SENT);
            message.setMessageText(messageText);
            message.setUserType(UserType.SELF);
            message.setBotIcon(botStatus);
            message.setMessageTime(new Date().getTime());
            chatMessages.add(message);
            listAdapter.notifyDataSetChanged();
        }
    }
    private void funcSendMessage(String messageText)
    {
        if(messageText.trim().length()==0)
            return;
        final ChatMessage message = new ChatMessage();
        message.setMessageStatus(Status.SENT);
        message.setMessageText(messageText);
        message.setUserType(UserType.OTHER);  // Receiver data
        message.setMessageTime(new Date().getTime());
        chatMessages.add(message);
        if(listAdapter!=null)
            listAdapter.notifyDataSetChanged();
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String selected =((TextView)view.findViewById(R.id.message_text)).getText().toString();
        chatEditText1.setText(selected);
    }

    @Override
    protected void onStop() {
        super.onStop();
    //    finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }



}
