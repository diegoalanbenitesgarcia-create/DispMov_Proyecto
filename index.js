const express = require('express');
const cors = require('cors');
const db = require('./db');

const app = express();
app.use(cors());
app.use(express.json());

// Ruta de prueba
app.get('/', (req, res) => {
  res.send('¡La API está viva!');
});

// Traer todas las tareas, con el nombre del área en vez del número
app.get('/tareas', async (req, res) => {
  const [rows] = await db.query(`
    SELECT t.id_tarea AS id, t.descripcion, a.nombre AS area,
           t.prioridad, t.estado, t.fecha_limite, t.tiempo_estimado
    FROM tareas t
    JOIN areas a ON t.id_area = a.id_area
  `);
  res.json(rows);
});

// Traer todos los trabajadores, con su habilidad principal y carga estimada
app.get('/trabajadores', async (req, res) => {
  const [rows] = await db.query(`
    SELECT u.nombre AS nombre, a.nombre AS area, p.nombre AS puesto,
           (SELECT h.nombre FROM trabajador_habilidades th
            JOIN habilidades h ON h.id_habilidad = th.id_habilidad
            WHERE th.id_trabajador = t.id_trabajador
            ORDER BY FIELD(th.nivel,'Avanzado','Intermedio','Básico') LIMIT 1) AS habilidadPrincipal,
           t.disponibilidad AS estado,
           LEAST(100, COALESCE((
             SELECT COUNT(*) FROM asignaciones asg
             WHERE asg.id_trabajador = t.id_trabajador AND asg.fecha_finalizacion IS NULL
           ) * 20, 0)) AS carga
    FROM trabajadores t
    JOIN usuarios u ON u.id_usuario = t.id_usuario
    JOIN areas a ON a.id_area = t.id_area
    JOIN puestos p ON p.id_puesto = t.id_puesto
  `);
  res.json(rows);
});

// POST asignar tarea
app.post('/asignaciones', async (req, res) => {
  const { id_tarea, id_trabajador } = req.body;
  await db.query(
    `INSERT INTO asignaciones (id_tarea, id_trabajador, fecha_asignacion) VALUES (?, ?, NOW())`,
    [id_tarea, id_trabajador]
  );
  res.json({ ok: true });
});

app.listen(3000, () => {
  console.log('Servidor corriendo en http://localhost:3000');
});