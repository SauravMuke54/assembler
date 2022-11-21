import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.io.IOException;


class TableRow {
	String symbol;
	int address;
	int index;

	public TableRow(String symbol, int address) {
		super();
		this.symbol = symbol;
		this.address = address;

	}
	public TableRow(String symbol, int address,int index) {
		super();
		this.symbol = symbol;
		this.address = address;
		this.index=index;

	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public int getAddress() {
		return address;
	}
	public void setAddress(int address) {
		this.address = address;
	}


}

public class PassTwo{
	ArrayList<TableRow> SYMTAB,LITTAB;

	public PassTwo()
	{
		SYMTAB=new ArrayList<>();
		LITTAB=new ArrayList<>();
	}
	public static void main(String[] args) {
		PassTwo PassTwo=new PassTwo();
		
		try {
			PassTwo.generateCode("IC.txt");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void readtables()
	{
		BufferedReader br;
		String line;
		try
		{
			br=new BufferedReader(new FileReader("SYMTAB.txt"));
			while((line=br.readLine())!=null)
			{
				String parts[]=line.split("\\s+");
				SYMTAB.add(new TableRow(parts[1], Integer.parseInt(parts[2]),Integer.parseInt(parts[0]) ));
			}
			br.close();
			br=new BufferedReader(new FileReader("LITTAB.txt"));
			while((line=br.readLine())!=null)
			{
				String parts[]=line.split("\\s+");
				LITTAB.add(new TableRow(parts[1], Integer.parseInt(parts[2]),Integer.parseInt(parts[0])));
			}
			br.close();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	void printOP() throws IOException{
	
	
		System.out.println("\n Pass One Two Output\n ");
		try (BufferedReader br = new BufferedReader(new FileReader("PassTwo.txt"))) {
   		String line;
   		while ((line = br.readLine()) != null) {
       		System.out.println(line);
   			}
		}
		}


	public void generateCode(String filename) throws Exception
	{
		readtables();
		BufferedReader br=new BufferedReader(new FileReader(filename));

		BufferedWriter bw=new BufferedWriter(new FileWriter("PassTwo.txt"));
		String line,code;
		while((line=br.readLine())!=null)
		{
			String parts[]=line.split("\\s+");
			
			if(parts[0].contains("AD")||parts[0].contains("DL,02"))
			{
				bw.write("\n");
				continue;
			}
			else if(parts.length==2)
			{
				if(parts[0].contains("DL")) //DC INSTR
				{
					parts[0]=parts[0].replaceAll("[^0-9]", "");
					System.out.println("Parts[0]"+parts[0]);
					if(Integer.parseInt(parts[0])==1)
					{
						int constant=Integer.parseInt(parts[1].replaceAll("[^0-9]", ""));
						code="00\t0\t"+String.format("%03d", constant)+"\n";
						bw.write(code);
						
						
					}
				}
				else if(parts[0].contains("IS"))
				{
					int opcode=Integer.parseInt(parts[0].replaceAll("[^0-9]", ""));
					if(opcode==10)
					{
						if(parts[1].contains("S"))
						{
							int symIndex=Integer.parseInt(parts[1].replaceAll("[^0-9]", ""));
							code=String.format("%02d", opcode)+"\t0\t"+String.format("%03d", SYMTAB.get(symIndex-1).getAddress())+"\n";
							bw.write(code);
						}
						else if(parts[1].contains("L"))
						{
							int symIndex=Integer.parseInt(parts[1].replaceAll("[^0-9]", ""));
							code=String.format("%02d", opcode)+"\t0\t"+String.format("%03d", LITTAB.get(symIndex-1).getAddress())+"\n";
							bw.write(code);
						}
						
					}
				}
			}
			else if(parts.length==1 && parts[0].contains("IS"))
			{
				int opcode=Integer.parseInt(parts[0].replaceAll("[^0-9]", ""));
				code=String.format("%02d", opcode)+"\t0\t"+String.format("%03d", 0)+"\n";
				bw.write(code);
			}
			else if(parts[0].contains("IS") && parts.length==3) //All OTHER IS INSTR
			{
			int opcode=	Integer.parseInt(parts[0].replaceAll("[^0-9]", ""));
			
			int regcode=Integer.parseInt(parts[1]);
			
			if(parts[2].contains("S"))
			{
				int symIndex=Integer.parseInt(parts[2].replaceAll("[^0-9]", ""));
				code=String.format("%02d", opcode)+"\t"+regcode+"\t"+String.format("%03d", SYMTAB.get(symIndex-1).getAddress())+"\n";
				bw.write(code);
			}
			else if(parts[2].contains("L"))
			{
				int symIndex=Integer.parseInt(parts[2].replaceAll("[^0-9]", ""));
				code=String.format("%02d", opcode)+"\t"+regcode+"\t"+String.format("%03d", LITTAB.get(symIndex-1).getAddress())+"\n";
				bw.write(code);
			}		
			}	
		}
		bw.close();
		br.close();
		printOP();
	}
}
