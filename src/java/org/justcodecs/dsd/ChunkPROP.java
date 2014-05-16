package org.justcodecs.dsd;

import java.io.IOException;

import org.justcodecs.dsd.Decoder.DecodeException;

public class ChunkPROP extends BaseChunk {
	int sampleRate;
	int channels;
	String comp;
	ChunkDSD dsd;
	long bound;
	
	@Override
	void read(DSDStream ds) throws DecodeException {		
		super.read(ds);
		try {
			ds.readFully(IDBuf, 0, 4);
			if ("SND ".equals(new String(IDBuf)) == false)
				throw new DecodeException("PROP chunk isn't SND", null);
			for (;;) {
				// read local chinks
				BaseChunk c = BaseChunk.create(ds, this);
				if (c instanceof ChunkFS)
					sampleRate = ((ChunkFS)c).sampleRate;
				else if (c instanceof ChunkCHNL)
					channels = ((ChunkCHNL)c).numChannels;
				else if (c instanceof ChunkCMPR)
					comp = ((ChunkCMPR)c).compression;
				else if (c instanceof ChunkDSD) {
					dsd = (ChunkDSD)c;
					//break;
				}
				//System.out.printf("--->%s at %d s+s%d%n", c, ds.getFilePointer(), c.start+c.size);
				if (ds.getFilePointer() >= parent.start + parent.size)
					break;
				
			}
		} catch (IOException e) {
			throw new DecodeException("", e);
		}
	}

	@Override
	public String toString() {
		return "ChunkPROP [sampleRate=" + sampleRate + ", channels=" + channels + ", comp=" + comp + "]";
	}
	
}