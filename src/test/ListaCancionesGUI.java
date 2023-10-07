package test;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import entities.Cancion;
import entities.ListaCanciones;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class ListaCancionesGUI extends JFrame {
	private DefaultTableModel tableModel;
	private JTable table;
	private ListaCanciones listaCanciones;

	public ListaCancionesGUI(ListaCanciones listaCanciones) {
		this.listaCanciones = listaCanciones;

		// Configurar la ventana
		setTitle("Lista de Canciones");
		setSize(800, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Crear un modelo de tabla
		String[] columnNames = { "ID", "Título", "Artista", "Álbum", "Revisado", "Rate" };
		tableModel = new DefaultTableModel(columnNames, 0);

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
		
		// Especificar un comparador personalizado para las columnas numéricas (ID y Rate)
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
		        // Aquí puedes agregar la lógica para guardar los cambios en la tabla
		        // Puedes recorrer la tabla y actualizar la lista de canciones si es necesario
		        // Por ejemplo:
		        // for (int row = 0; row < tableModel.getRowCount(); row++) {
		        //     int id = (int) tableModel.getValueAt(row, 0);
		        //     // Actualizar la lista de canciones con los valores de la tabla
		        //     // ...
		        // }
		        // Asegúrate de implementar la lógica de guardar los cambios según tus necesidades.
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
