package com.drogueria.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DeliveryNoteConfigFile {

	private FileReader fileReader;
	private final BufferedReader bufferReader;
	private final File deliveryNoteConfigFile;

	private final Map<String, Coordinate> properties;
	private int ancho;
	private int alto;
	private int lineaDetalle;
	private int incremento;
	private int copias;

	public DeliveryNoteConfigFile(String filepath) {

		this.properties = new HashMap<String, Coordinate>();

		new File(filepath).mkdirs();
		this.deliveryNoteConfigFile = new File(filepath + "FMT_re.dat");

		try {
			if (!this.deliveryNoteConfigFile.exists()) {
				this.deliveryNoteConfigFile.createNewFile();
			}

			this.fileReader = new FileReader(this.deliveryNoteConfigFile.getAbsolutePath());
		} catch (IOException e) {
			throw new RuntimeException("No se ha podido abrir/crear el archivo " + this.deliveryNoteConfigFile.getAbsolutePath() + ".", e);
		}
		this.bufferReader = new BufferedReader(this.fileReader);

		this.read();
		this.close();
	}

	private void read() {
		String line = null;
		try {
			while ((line = this.bufferReader.readLine()) != null) {
				if (line.matches("^\\[ANCHO\\].+")) {
					line = line.replaceAll("\\x20", "");
					this.ancho = Integer.parseInt(line.substring(7));
				} else if (line.matches("^\\[ALTO\\].+")) {
					line = line.replaceAll("\\x20", "");
					this.alto = Integer.parseInt(line.substring(6));
				} else if (line.matches("^\\[LINEADETALLE\\].+")) {
					line = line.replaceAll("\\x20", "");
					this.lineaDetalle = Integer.parseInt(line.substring(14));
				} else if (line.matches("^\\[INCREMENTO\\].+")) {
					line = line.replaceAll("\\x20", "");
					this.incremento = Integer.parseInt(line.substring(12));
				} else if (line.matches("^\\[COPIAS\\].+")) {
					line = line.replaceAll("\\x20", "");
					this.copias = Integer.parseInt(line.substring(8));
				}
				// parseo coordenadas de elementos propiamente dichos.
				else {
					if (line.matches("^\\d{6}@.+")) {
						this.properties.put(line.substring(7), new Coordinate(Float.parseFloat(line.substring(0, 3)), Float.parseFloat(line.substring(3, 6))));
					}
				}
			}

		} catch (IOException e) {
			throw new RuntimeException("No se ha podido procesar la linea " + line, e);
		}
	}

	private void close() {
		try {
			this.bufferReader.close();
		} catch (IOException e) {
			throw new RuntimeException("No se ha podido cerrar y guardar el archivo.", e);
		}
	}

	public FileReader getFileReader() {
		return this.fileReader;
	}

	public BufferedReader getBufferReader() {
		return this.bufferReader;
	}

	public File getDeliveryNoteConfigFile() {
		return this.deliveryNoteConfigFile;
	}

	public int getAncho() {
		return this.ancho;
	}

	public int getAlto() {
		return this.alto;
	}

	public int getLineaDetalle() {
		return this.lineaDetalle;
	}

	public int getIncremento() {
		return this.incremento;
	}

	public int getCopias() {
		return this.copias;
	}

	public Coordinate getDateCoordinate() {
		return this.properties.get("FECHA");
	}

	public Coordinate getClientCoordinate() {
		return this.properties.get("CLIENTE");
	}

	public Coordinate getAddressCoordinate() {
		return this.properties.get("DOMICILIO");
	}

	public Coordinate getLocalityCoordinate() {
		return this.properties.get("LOCALIDAD");
	}

	public Coordinate getTaxConditionCoordinate() {
		return this.properties.get("CONDIVA");
	}

	public Coordinate getTaxIdCoordinate() {
		return this.properties.get("CUIT");
	}

	public Coordinate getDeliveryObservation1Coordinate() {
		return this.properties.get("OBS1-E");
	}

	public Coordinate getOrderCoordinate() {
		return this.properties.get("PEDIDO");
	}

	public Coordinate getClientDeliveryCodeCoordinate() {
		return this.properties.get("CODCLIENTE-E");
	}

	public Coordinate getClientDeliveryCoordinate() {
		return this.properties.get("CLIENTE-E");
	}

	public Coordinate getDeliveryAddressCoordinate() {
		return this.properties.get("DOMICILIO-E");
	}

	public Coordinate getDeliveryLocalityCoordinate() {
		return this.properties.get("LOCALIDAD-E");
	}

	public Coordinate getDeliveryTaxConditionCoordinate() {
		return this.properties.get("CONDIVA-E");
	}

	public Coordinate getDeliveryTaxIdCoordinate() {
		return this.properties.get("CUIT-E");
	}

	public Coordinate getAmountDetailCoordinate() {
		return this.properties.get("CANTIDAD");
	}

	public Coordinate getCodeDetailCoordinate() {
		return this.properties.get("CODIGO");
	}

	public Coordinate getDescriptionDetailCoordinate() {
		return this.properties.get("DESCRIPCION");
	}

	public Coordinate getBrandDetailCoordinate() {
		return this.properties.get("MARCA");
	}

	public Coordinate getAmountItemsCoordinate() {
		return this.properties.get("CANTITEMS");
	}

	public Coordinate getObservation1Coordinate() {
		return this.properties.get("OBS1");
	}

	public Coordinate getObservation2Coordinate() {
		return this.properties.get("OBS2");
	}

	public Coordinate getObservation3Coordinate() {
		return this.properties.get("OBS3");
	}

	public Coordinate getObservation4Coordinate() {
		return this.properties.get("OBS4");
	}

	public Coordinate getObservation5Coordinate() {
		return this.properties.get("OBS5");
	}
}