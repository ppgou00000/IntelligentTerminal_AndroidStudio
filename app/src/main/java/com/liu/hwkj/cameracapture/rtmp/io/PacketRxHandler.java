package com.liu.hwkj.cameracapture.rtmp.io;

import com.liu.hwkj.cameracapture.rtmp.packets.RtmpPacket;

/**
 * Handler interface for received RTMP packets
 * @author francois
 */
public interface PacketRxHandler {
    
    public void handleRxPacket(RtmpPacket rtmpPacket);
    
    public void notifyWindowAckRequired(final int numBytesReadThusFar);    
}
