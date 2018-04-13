package br.ufpe.cin.if1001.rss;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MainActivity extends Activity {

    //ao fazer envio da resolucao, use este link no seu codigo!
    private final String RSS_FEED = "http://leopoldomt.com/if1001/g1brasil.xml";

    //OUTROS LINKS PARA TESTAR...
    //http://rss.cnn.com/rss/edition.rss
    //http://pox.globo.com/rss/g1/brasil/
    //http://pox.globo.com/rss/g1/ciencia-e-saude/
    //http://pox.globo.com/rss/g1/tecnologia/

    //use ListView ao invés de TextView - deixe o atributo com o mesmo nome

    // Declarando ListView e ArrayAdapter

    private ListView conteudoRSS;
    private ArrayAdapter<ItemRSS> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //use ListView ao invés de TextView - deixe o ID no layout XML com o mesmo nome conteudoRSS
        //isso vai exigir o processamento do XML baixado da internet usando o ParserRSS
        conteudoRSS = (ListView) findViewById(R.id.conteudoRSS);

    }

    @Override
    protected void onStart() {
        super.onStart();
        new CarregaRSStask().execute(RSS_FEED);
    }

    private class CarregaRSStask extends AsyncTask<String, Void, List<ItemRSS>> {
        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), "iniciando...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected List<ItemRSS> doInBackground(String... params) {
            String conteudo = "provavelmente deu erro...";

            List<ItemRSS> prss = null;


            try {
                conteudo = getRssFeed(params[0]);
                prss = ParserRSS.parse(conteudo);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

            return prss;
        }

        @Override
        protected void onPostExecute(List<ItemRSS> s) {
            Toast.makeText(getApplicationContext(), "terminando...", Toast.LENGTH_SHORT).show();

            //ajuste para usar uma ListView
            //o layout XML a ser utilizado esta em res/layout/itemlista.xml

            ItemRSS[] itrss = s.toArray(new ItemRSS[s.size()]);

            ItemRSSAdapter itemRSSAdapter = new ItemRSSAdapter(MainActivity.this, itrss);

            conteudoRSS.setAdapter(itemRSSAdapter);

            conteudoRSS.setOnClickListener();

            /*adapter = new ArrayAdapter<ItemRSS> (MainActivity.this,
                android.R.layout.simple_list_item_1,
                    s
            );

            conteudoRSS.setAdapter(adapter);*/


        }
    }



    // ArrayAdapter Personalizado

    class ItemRSSAdapter extends BaseAdapter {

        Context c;
        ItemRSS[] itemrss;

        public ItemRSSAdapter(Context c, ItemRSS[] itens) {
            this.c = c;
            this.itemrss = itens;
        }

        @Override
        public int getCount() {
            return itemrss.length;
        }

        @Override
        public Object getItem(int position) {
            return itemrss[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = LayoutInflater.
                    from(c).
                    inflate(R.layout.itemlista, parent, false);

            TextView tvTitulo = (TextView) v.findViewById(R.id.item_titulo);
            TextView tvData = (TextView) v.findViewById(R.id.item_data);

            String titulo = ((ItemRSS) getItem(position)).getTitle();
            String data = ((ItemRSS) getItem(position)).getPubDate();

            tvTitulo.setText(titulo);
            tvData.setText(data);

            return v;
        }
    }




    //Opcional - pesquise outros meios de obter arquivos da internet
    private String getRssFeed(String feed) throws IOException {
        InputStream in = null;
        String rssFeed = "";
        try {
            URL url = new URL(feed);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            in = conn.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            for (int count; (count = in.read(buffer)) != -1; ) {
                out.write(buffer, 0, count);
            }
            byte[] response = out.toByteArray();
            rssFeed = new String(response, "UTF-8");
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return rssFeed;
    }
}
