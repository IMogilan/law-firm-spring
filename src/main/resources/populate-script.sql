
-- Populate lawyers table
INSERT INTO public.lawyers (first_name, last_name, job_title, hourly_rate)
VALUES ('John', 'Doe', 'ASSOCIATE', 100.00),
       ('Marta', 'Doe', 'PARTNER', 150.00),
       ('Chris', 'Doe', 'MANAGING_PARTNER', 200.00),
       ('Jane', 'Smith', 'ASSOCIATE', 150.00),
       ('Max', 'Smith', 'PARTNER', 200.00),
       ('Piter', 'Smith', 'MANAGING_PARTNER', 250.00),
       ('Gerbert', 'Johnson', 'ASSOCIATE', 200.00),
       ('Robert', 'Johnson', 'PARTNER', 250.00),
       ('Julia', 'Johnson', 'MANAGING_PARTNER', 300.00);

-- Populate clients table
INSERT INTO public.clients (name, description)
VALUES ('Apple', 'Corporate client with legal needs'),
       ('Elon M.', 'Individual seeking legal advice'),
       ('Peter&Mike', 'Startup company requiring legal services');

-- Populate tasks table
INSERT INTO public.tasks (title, description, priority, status, receipt_date, due_date, completion_date, hours_spent_on_task,
                   client_id)
VALUES ('Task 1', 'Review contract', 'HIGH', 'ACCEPTED', '2023-01-05', '2023-01-15', NULL, 5.0, 1),
       ('Task 2', 'Legal research', 'MEDIUM', 'IN_PROGRESS', '2023-02-01', '2023-02-10', NULL, 8.0, 2),
       ('Task 3', 'Draft legal documents', 'LOW', 'RECEIVED', '2023-03-15', '2023-03-25', '2023-03-20', 10.0, 3);

-- Populate lawyers_tasks table
INSERT INTO public.lawyers_tasks (lawyer_id, task_id)
VALUES (1, 1),
       (2, 2),
       (3, 3);