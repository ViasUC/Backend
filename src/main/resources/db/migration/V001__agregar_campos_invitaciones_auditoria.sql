-- Agregar campos para control de invitaciones en tabla auditoria
-- Fecha: 2025-11-25
-- Descripcion: Campos para rastrear invitaciones a candidatos para oportunidades/convenios

-- Agregar columnas si no existen
ALTER TABLE auditoria 
ADD COLUMN IF NOT EXISTS id_usuario INTEGER,
ADD COLUMN IF NOT EXISTS id_empresa INTEGER,
ADD COLUMN IF NOT EXISTS id_oportunidad INTEGER,
ADD COLUMN IF NOT EXISTS id_convenio INTEGER,
ADD COLUMN IF NOT EXISTS canal VARCHAR(20),
ADD COLUMN IF NOT EXISTS mensaje_enviado TEXT,
ADD COLUMN IF NOT EXISTS ip_address VARCHAR(50);

-- Agregar comentarios para documentacion
COMMENT ON COLUMN auditoria.id_usuario IS 'ID del candidato que recibe la invitacion';
COMMENT ON COLUMN auditoria.id_empresa IS 'ID de la empresa que envia la invitacion';
COMMENT ON COLUMN auditoria.id_oportunidad IS 'ID de la oportunidad relacionada (nullable)';
COMMENT ON COLUMN auditoria.id_convenio IS 'ID del convenio relacionado (nullable)';
COMMENT ON COLUMN auditoria.canal IS 'Canal de envio: EMAIL o WHATSAPP';
COMMENT ON COLUMN auditoria.mensaje_enviado IS 'Contenido del mensaje enviado';
COMMENT ON COLUMN auditoria.ip_address IS 'Direccion IP del usuario que realiza la accion';

-- Crear indices para mejorar rendimiento de consultas
CREATE INDEX IF NOT EXISTS idx_auditoria_usuario_oportunidad 
ON auditoria(id_usuario, id_oportunidad, accion) 
WHERE id_oportunidad IS NOT NULL;

CREATE INDEX IF NOT EXISTS idx_auditoria_usuario_convenio 
ON auditoria(id_usuario, id_convenio, accion) 
WHERE id_convenio IS NOT NULL;

CREATE INDEX IF NOT EXISTS idx_auditoria_empresa 
ON auditoria(id_empresa) 
WHERE id_empresa IS NOT NULL;

CREATE INDEX IF NOT EXISTS idx_auditoria_accion_fecha 
ON auditoria(accion, fecha_evento DESC);
