-- Table: public.annotations

CREATE SEQUENCE IF NOT EXISTS public.annotations_seq;

DROP TABLE IF EXISTS public.annotations;

CREATE TABLE IF NOT EXISTS public.annotations
(
    id integer NOT NULL,
    text character varying(255),
    url character varying(255),
    CONSTRAINT annotations_pkey PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS public.annotations
    OWNER to postgres;

-- Trigger notify function

CREATE OR REPLACE FUNCTION public.notify_trigger()
    RETURNS trigger
AS '
DECLARE
  rec RECORD;
  dat RECORD;
  payload TEXT;
BEGIN

  -- Set record row depending on operation
  CASE TG_OP
  WHEN ''UPDATE'' THEN
     rec := NEW;
     dat := OLD;
  WHEN ''INSERT'' THEN
     rec := NEW;
  WHEN ''DELETE'' THEN
     rec := OLD;
  ELSE
     RAISE EXCEPTION ''Unknown TG_OP: "%". Should not occur!'', TG_OP;
  END CASE;

  -- Build the payload
  payload := json_build_object(''timestamp'',CURRENT_TIMESTAMP(3),''action'',UPPER(TG_OP),''schema'',TG_TABLE_SCHEMA,''identity'',TG_TABLE_NAME,''record'',row_to_json(rec), ''old'',row_to_json(dat));

  -- Notify the channel
  PERFORM pg_notify(''annotations_db_event'',payload);

  RETURN rec;
END;
' LANGUAGE plpgsql;

ALTER FUNCTION public.notify_trigger()
    OWNER TO postgres;

--
--DROP TRIGGER annotations_notify ON annotations;
--
CREATE TRIGGER annotations_events_trigger
    AFTER INSERT OR DELETE OR UPDATE
    ON annotations
    FOR EACH ROW
    EXECUTE FUNCTION public.notify_trigger();
