package com.liu.hwkj.cameracapture.rtmp.packets;

import com.liu.hwkj.cameracapture.rtmp.amf.AmfBoolean;
import com.liu.hwkj.cameracapture.rtmp.amf.AmfData;
import com.liu.hwkj.cameracapture.rtmp.amf.AmfDecoder;
import com.liu.hwkj.cameracapture.rtmp.amf.AmfNull;
import com.liu.hwkj.cameracapture.rtmp.amf.AmfNumber;
import com.liu.hwkj.cameracapture.rtmp.amf.AmfString;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * RTMP packet with a "variable" body structure (i.e. the structure of the
 * body depends on some other state/parameter in the packet.
 * 
 * Examples of this type of packet are Command and Data; this abstract class
 * exists mostly for code re-use.
 * 
 * @author francois
 */
public abstract class VariableBodyRtmpPacket extends RtmpPacket {

    protected List<AmfData> data;

    public VariableBodyRtmpPacket(RtmpHeader header) {
        super(header);
    }

    public List<AmfData> getData() {
        return data;
    }

    public void addData(String string) {
        addData(new AmfString(string));
    }

    public void addData(double number) {
        addData(new AmfNumber(number));
    }
    
    public void addData(boolean bool) {
        addData(new AmfBoolean(bool));
    }

    public void addData(AmfData dataItem) {
        if (data == null) {
            this.data = new ArrayList<AmfData>();
        }
        if (dataItem == null) {
            dataItem = new AmfNull();
        }
        this.data.add(dataItem);
    }

    protected void readVariableData(final InputStream in, int bytesAlreadyRead) throws IOException {
        // ...now read in arguments (if any)
        do {
            AmfData dataItem = AmfDecoder.readFrom(in);
            addData(dataItem);
            bytesAlreadyRead += dataItem.getSize();
        } while (bytesAlreadyRead < header.getPacketLength());
    }

    protected void writeVariableData(final OutputStream out) throws IOException {
        if (data != null) {
            for (AmfData dataItem : data) {
                dataItem.writeTo(out);
            }
        } else {
            // Write a null
            AmfNull.writeNullTo(out);
        }
    }
}
