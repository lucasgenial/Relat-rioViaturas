/**
 * Author:  Lucas Matos e Souza
 * Created: 26/07/2017
 */

create database sisef_atual;
create user 'cicom' identified by 'nac@2016';
grant all privileges on sisef_atual.* to cicom;
