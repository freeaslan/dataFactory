set names utf8;

DELETE from dfp_apis where project_name in (select name from dfp_menu where parent_id=1);
DELETE from dfp_env_params  where project_name='example';
DELETE from dfp_module where project_name in (select name from dfp_menu where parent_id=1 or name='demo');
DELETE from dfp_public_param where id in (1,2);
DELETE from dfp_scene_params where scene_id in (select id from dfp_scene where project_name in (select name from dfp_menu where parent_id=1 or name='demo'));
DELETE from dfp_scene_request where scene_id in (select id from dfp_scene where project_name in (select name from dfp_menu where parent_id=1 or name='demo'));
DELETE from dfp_scene where project_name in (select name from dfp_menu where parent_id=1 or name='demo');
DELETE from dfp_menu where  parent_id=1 or name='demo';