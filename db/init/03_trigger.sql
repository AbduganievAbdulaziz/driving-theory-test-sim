----------------------------------------------------------------------------------------------------
create or replace function update_modified_at()
  returns trigger as $$
begin 
  new.modified_at = now();
  return new;
end;
$$ language plpgsql;

----------------------------------------------------------------------------------------------------
-- languages table
----------------------------------------------------------------------------------------------------
create or replace trigger languages_modified_at_trg 
  before update 
    on languages
  for each row 
    execute function update_modified_at();

----------------------------------------------------------------------------------------------------
-- questions table
----------------------------------------------------------------------------------------------------
create or replace trigger questions_modified_at_trg 
  before update 
    on questions
  for each row 
    execute function update_modified_at();

----------------------------------------------------------------------------------------------------
-- question_contents table
----------------------------------------------------------------------------------------------------
create or replace trigger question_contents_modified_at_trg
  before update 
    on question_contents
  for each row 
    execute function update_modified_at();

----------------------------------------------------------------------------------------------------
-- options table
----------------------------------------------------------------------------------------------------
create or replace trigger options_modified_at_trg 
  before update 
    on options
  for each row 
    execute function update_modified_at();

----------------------------------------------------------------------------------------------------
-- option_contents table
----------------------------------------------------------------------------------------------------
create or replace trigger option_contents_modified_at_trg 
  before update 
    on option_contents
  for each row 
    execute function update_modified_at();

----------------------------------------------------------------------------------------------------
-- templates table
----------------------------------------------------------------------------------------------------
create or replace trigger templates_modified_at_trg 
  before update 
    on templates
  for each row 
    execute function update_modified_at();

----------------------------------------------------------------------------------------------------
-- template_questions table
----------------------------------------------------------------------------------------------------
create or replace trigger template_questions_modified_at_trg 
  before update 
    on template_questions
  for each row 
    execute function update_modified_at();