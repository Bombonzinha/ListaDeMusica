package test;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import entities.Cancion;
import entities.ListaCanciones;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ListaCancionesGUI extends JFrame {
	private DefaultTableModel tableModel;
	private JTable table;
	private ListaCanciones listaCanciones;
	private double[] columnaProporciones = { 0.03, 0.45, 0.25, 0.21, 0.03, 0.02 }; // Proporciones de ancho de las
																					// columnas

	public ListaCancionesGUI(ListaCanciones listaCanciones) {
		this.listaCanciones = listaCanciones;

		// Configurar la ventana
		setTitle("Lista de Canciones");
		setSize(800, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Crear un modelo de tabla personalizado
		String[] columnNames = { "ID", "Título", "Artista", "Álbum", "Revisado", "Rate" };
		tableModel = new DefaultTableModel(columnNames, 0) {
			@Override
			public Class<?> getColumnClass(int columnIndex) {
				// Define la clase de columna para permitir valores booleanos en la columna
				// "Revisado"
				if (columnIndex == 4) {
					return Boolean.class;
				}
				return super.getColumnClass(columnIndex);
			}
		};

		// Llenar la tabla con datos
		for (Cancion cancion : listaCanciones.getListaCanciones()) {
			Object[] rowData = { cancion.getId(), cancion.getTitulo(), cancion.getArtista(), cancion.getAlbum(),
					cancion.isRevisado(), cancion.getRate() };
			tableModel.addRow(rowData);
		}

		// Crear la tabla
		table = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);
		
		

		// Configurar la columna "REVISADO" para usar la celda personalizada
		table.getColumnModel().getColumn(4).setCellRenderer(new CheckBoxCellRenderer());

		// Crear un TableCellRenderer personalizado para alinear los valores de las
		// columnas a la derecha
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);

		// Aplicar el TableCellRenderer personalizado a las columnas deseadas
		table.getColumnModel().getColumn(0).setCellRenderer(rightRenderer); // Alinea la columna 0 (ID)
		table.getColumnModel().getColumn(1).setCellRenderer(rightRenderer); // Alinea la columna 4 (Revisado)
		table.getColumnModel().getColumn(2).setCellRenderer(rightRenderer); // Alinea la columna 4 (Revisado)
		table.getColumnModel().getColumn(3).setCellRenderer(rightRenderer); // Alinea la columna 4 (Revisado)
		table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer); // Alinea la columna 5 (Rate)

		// Agregar un ComponentListener para ajustar el ancho de las columnas cuando
		// cambia el tamaño de la ventana
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				// Ajustar el ancho de las columnas aquí en función del tamaño de la ventana
				int totalWidth = getWidth();
				int columnCount = table.getColumnModel().getColumnCount();
				for (int i = 0; i < columnCount; i++) {
					int preferredWidth = (int) (totalWidth * columnaProporciones[i]);
					table.getColumnModel().getColumn(i).setPreferredWidth(preferredWidth);
				}
			}
		});

		// Crear un TableRowSorter y asignarlo a la tabla
		TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
		table.setRowSorter(sorter);

		// Permitir que el usuario haga clic en el encabezado de la columna para ordenar
		table.getTableHeader().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int columnIndex = table.columnAtPoint(e.getPoint());
				sorter.toggleSortOrder(columnIndex); // Alterna entre orden ascendente y descendente
			}
		});

		// Especificar un comparador personalizado para las columnas numéricas (ID y
		// Rate)
		sorter.setComparator(0, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o1.compareTo(o2);
			}
		});

		sorter.setComparator(5, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o1.compareTo(o2);
			}
		});

		// Agregar un listener a las celdas de la tabla
		table.getModel().addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				int row = e.getFirstRow();
				int column = e.getColumn();
				if (row >= 0 && column >= 0) {
					String columnName = table.getColumnName(column);
					Object newValue = tableModel.getValueAt(row, column);
					int id = (int) tableModel.getValueAt(row, 0);

					// Actualizar la lista de canciones con los nuevos valores
					Cancion cancion = listaCanciones.traerCancion(id);
					if (cancion != null) {
						switch (columnName) {
						case "Título":
							cancion.setTitulo(newValue.toString());
							break;
						case "Artista":
							cancion.setArtista(newValue.toString());
							break;
						case "Álbum":
							cancion.setAlbum(newValue.toString());
							break;
						case "Revisado":
							cancion.setRevisado((boolean) newValue);
							break;
						case "Rate":
							cancion.setRate(Integer.parseInt(newValue.toString()));
							break;
						default:
							break;
						}
					}
				}
			}
		});

		// Crear el botón "GUARDAR"
		JButton guardarButton = new JButton("GUARDAR");
		guardarButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Test.guardar(listaCanciones);
				// Llamar al método para guardar la lista en un archivo de Excel
		        guardarListaComoExcel("ListaCanciones.xlsx"); // Puedes cambiar el nombre del archivo según tus preferencias
		    
			}
		});

		// Agregar el botón "GUARDAR" en la parte inferior
		add(guardarButton, BorderLayout.SOUTH);

	}

	private void actualizarCancion(int rowIndex, String columnName, String newValue) {
		// Obtener la canción correspondiente
		Cancion cancion = listaCanciones.getListaCanciones().get(rowIndex);

		// Actualizar el campo correspondiente en la canción
		switch (columnName) {
		case "ID":
			cancion.setId(Integer.parseInt(newValue));
			break;
		case "Título":
			cancion.setTitulo(newValue);
			break;
		case "Artista":
			cancion.setArtista(newValue);
			break;
		case "Álbum":
			cancion.setAlbum(newValue);
			break;
		case "Revisado":
			cancion.setRevisado(Boolean.parseBoolean(newValue));
			break;
		case "Rate":
			cancion.setRate(Integer.parseInt(newValue));
			break;
		default:
			break;
		}
	}
	
	private void guardarListaComoExcel(String nombreArchivo) {
	    try {
	        Workbook workbook = new XSSFWorkbook(); // Crear un nuevo libro de Excel
	        Sheet sheet = workbook.createSheet("Lista de Canciones"); // Crear una hoja de trabajo

	        // Agregar encabezados de columna
	        Row headerRow = sheet.createRow(0);
	        String[] columnNames = { "ID", "Título", "Artista", "Álbum", "Revisado", "Rate" };
	        for (int i = 0; i < columnNames.length; i++) {
	            Cell cell = headerRow.createCell(i);
	            cell.setCellValue(columnNames[i]);
	        }

	        // Agregar datos de la lista a las filas
	        List<Cancion> lista = listaCanciones.getListaCanciones();
	        int rowNum = 1; // Comenzar desde la segunda fila
	        for (Cancion cancion : lista) {
	            Row row = sheet.createRow(rowNum++);
	            row.createCell(0).setCellValue(cancion.getId());
	            row.createCell(1).setCellValue(cancion.getTitulo());
	            row.createCell(2).setCellValue(cancion.getArtista());
	            row.createCell(3).setCellValue(cancion.getAlbum());
	            row.createCell(4).setCellValue(cancion.isRevisado());
	            row.createCell(5).setCellValue(cancion.getRate());
	        }

	        // Guardar el libro de Excel en un archivo
	        FileOutputStream outputStream = new FileOutputStream(nombreArchivo);
	        workbook.write(outputStream);
	        outputStream.close();

	        JOptionPane.showMessageDialog(this, "La lista se ha guardado en Excel correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
	    } catch (Exception e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(this, "Ocurrió un error al guardar la lista en Excel.", "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}


	public static void main(String[] args) {
		// Crear una instancia de ListaCanciones y agregar canciones de ejemplo
		ListaCanciones listaCanciones = Test.cargar();
//        listaCanciones.agregarCanción("Canción 1", "Artista 1", "Álbum 1", true, 0);
//        listaCanciones.agregarCanción("Canción 2", "Artista 2", "Álbum 2", false, 0);
//        listaCanciones.agregarCanción("Canción 3", "Artista 3", "Álbum 3", true, 0);

		// Crear la ventana de la interfaz gráfica y pasar la lista de canciones
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new ListaCancionesGUI(listaCanciones).setVisible(true);
			}
		});
	}

}
