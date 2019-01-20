package com.papo.papolampion;

import com.papo.lib.Laumio;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.EventListener;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

public class BotListener extends ListenerAdapter {

    private Laumio laumio;
    private JDA jda;

    public BotListener(Laumio laumio, JDA jda)
    {
        this.laumio = laumio;
        this.jda = jda;
    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        super.onPrivateMessageReceived(event);

        String[] colors = event.getMessage().getContentRaw().split(Pattern.quote(" "));

        try {
            laumio.fill(new HashSet<String>(Arrays.asList("Laumio_1D9486")), Integer.parseInt(colors[0]), Integer.parseInt(colors[1]), Integer.parseInt(colors[2]));
        } catch (MqttException e) {
            e.printStackTrace();
        }

        jda.getGuilds().get(0).getDefaultChannel().sendMessage("Changement de couleur d'une lampe").queue();
    }

    public void publish(String str)
    {
        jda.getGuilds().get(0).getDefaultChannel().sendMessage(str).queue();
    }
}
