package com.github.hydrazine.module.builtin;

import com.github.hydrazine.Hydrazine;
import com.github.hydrazine.minecraft.Server;
import com.github.hydrazine.module.Module;
import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.protocol.MinecraftConstants;
import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.mc.protocol.data.SubProtocol;
import com.github.steveice10.mc.protocol.data.status.ServerStatusInfo;
import com.github.steveice10.mc.protocol.data.status.handler.ServerInfoHandler;
import com.github.steveice10.mc.protocol.data.status.handler.ServerPingTimeHandler;
import com.github.steveice10.packetlib.Client;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.tcp.TcpSessionFactory;

/**
 * 
 * @author xTACTIXzZ
 * 
 * This module retrieves information of a minecraft server
 *
 */
public class InfoModule implements Module
{
	
	private int hasRetrieved = 0;
	
	@Override
	public String getName() 
	{
		return "info";
	}

	@Override
	public String getDescription() 
	{
		return "Retrieves information about a minecraft server.";
	}
	
	@Override
	public void start() 
	{
		if(!Hydrazine.settings.hasSetting("host") || Hydrazine.settings.getSetting("host") == null)
		{
			System.out.println(Hydrazine.errorPrefix + "You have to specify a server to get the information from (-h)");
			
			System.exit(1);
		}
		
		Server server = new Server(Hydrazine.settings.getSetting("host"), Integer.parseInt(Hydrazine.settings.getSetting("port")));
		MinecraftProtocol protocol = new MinecraftProtocol(SubProtocol.STATUS);
        Client client = new Client(server.getHost(), server.getPort(), protocol, new TcpSessionFactory());
                                
        client.getSession().setFlag(MinecraftConstants.SERVER_INFO_HANDLER_KEY, new ServerInfoHandler() 
        {
            @Override
            public void handle(Session session, ServerStatusInfo info) 
            {
                System.out.println(Hydrazine.infoPrefix + "Version: " + info.getVersionInfo().getVersionName() + ", Protocol Version: " + info.getVersionInfo().getProtocolVersion());
                System.out.println(Hydrazine.infoPrefix + "Player Count: " + info.getPlayerInfo().getOnlinePlayers() + " / " + info.getPlayerInfo().getMaxPlayers());
                System.out.print(Hydrazine.infoPrefix + "Players: ");
                
                for(GameProfile gp : info.getPlayerInfo().getPlayers())
                {
                	System.out.print(gp.getName() + " ");
                }
                                
                System.out.println("\n" + Hydrazine.infoPrefix + "Description: " + info.getDescription().getFullText());
                                
                hasRetrieved++;
            }
        });

        client.getSession().setFlag(MinecraftConstants.SERVER_PING_TIME_HANDLER_KEY, new ServerPingTimeHandler() 
        {
            @Override
            public void handle(Session session, long pingTime) 
            {
                System.out.println(Hydrazine.infoPrefix + "Server Ping: " + pingTime + "ms");
                
                hasRetrieved++;
            }
        });

        client.getSession().connect();
        
        while(hasRetrieved != 2) 
        {
            try 
            {
                Thread.sleep(5);
            } 
            catch(InterruptedException e) 
            {
                e.printStackTrace();
            }
        }
        
        client.getSession().disconnect(Hydrazine.infoPrefix + "Retrieved server information.");
	}

	@Override
	public void stop(String cause)
	{
		System.out.println(Hydrazine.infoPrefix + "Stopping module " + getName() + ": " + cause);
		
		System.exit(0);
	}

	@Override
	public void configure() 
	{
		System.out.println(Hydrazine.infoPrefix + "This module can't be configured.");
	}
}
