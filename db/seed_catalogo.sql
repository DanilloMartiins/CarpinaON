-- ============================================================================
-- SEED DO CATÁLOGO - CarpinaON
-- Fonte: mock_data.dart do app Flutter (dados prontos do Isael)
-- Banco: Neon (produção) ou local (carpinaon-db)
-- Como rodar: colar no SQL editor do Neon ou rodar via psql
-- Idempotente: pode rodar mais de uma vez que não duplica
-- NOTA: cor e icone da categoria ficam hardcodados no Flutter (não vêm da API), por isso não estão no INSERT
-- ============================================================================

-- ----------------------------------------------------------------------------
-- 1. CATEGORIAS
-- ----------------------------------------------------------------------------
INSERT INTO categoria (nome, descricao, ativo)
SELECT 'Zeladoria & Obras',
       'Acompanhamento e solicitação de reparos estruturais em vias públicas, manutenção da rede de iluminação da cidade, coleta seletiva e zeladoria de praças.',
       true
WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE nome = 'Zeladoria & Obras');

INSERT INTO categoria (nome, descricao, ativo)
SELECT 'Saúde Pública',
       'Acesso aos serviços da Atenção Básica de Saúde, agendamentos em Unidades Básicas de Saúde (UBS), Cartão SUS e Vigilância Sanitária.',
       true
WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE nome = 'Saúde Pública');

INSERT INTO categoria (nome, descricao, ativo)
SELECT 'Educação',
       'Gestão escolar municipal, solicitação de vagas em creches, acompanhamento de boletins, merenda e transporte escolar para estudantes de Carpina.',
       true
WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE nome = 'Educação');

INSERT INTO categoria (nome, descricao, ativo)
SELECT 'Fazenda & Tributos',
       'Serviços de arrecadação, emissão de guias (IPTU/ISS), Certidões Negativas, ITBI e atendimento exclusivo à Sala do Empreendedor de Carpina.',
       true
WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE nome = 'Fazenda & Tributos');

INSERT INTO categoria (nome, descricao, ativo)
SELECT 'Social & Cuidado Animal',
       'Programas de assistência social, acesso ao CRAS, Cadastro Único e ações voltadas ao bem-estar e controle populacional de animais.',
       true
WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE nome = 'Social & Cuidado Animal');

INSERT INTO categoria (nome, descricao, ativo)
SELECT 'Mobilidade & Trânsito',
       'Gerenciamento do trânsito local, credenciamento para vagas especiais, recursos de multas, sinalização e passe livre universitário.',
       true
WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE nome = 'Mobilidade & Trânsito');

-- ----------------------------------------------------------------------------
-- 2. SERVIÇOS
-- ----------------------------------------------------------------------------

-- === Zeladoria & Obras ===
INSERT INTO servico (categoria_id, nome, descricao, quando_usar, nao_usar_para, requer_endereco, ativo, form_type, estimated_days)
SELECT c.id, 'Reparo de Iluminação Pública',
       'Solicite a troca de lâmpadas queimadas ou piscando em postes.',
       'Utilize para relatar lâmpadas queimadas, acesas durante o dia ou piscando. Informe a rua e número.',
       'NÃO utilize para problemas na fiação interna da residência ou medidores da NEOENERGIA.',
       true, true, 'form_light_repair', 3
FROM categoria c
WHERE c.nome = 'Zeladoria & Obras'
  AND NOT EXISTS (SELECT 1 FROM servico s WHERE s.nome = 'Reparo de Iluminação Pública');

INSERT INTO servico (categoria_id, nome, descricao, quando_usar, nao_usar_para, requer_endereco, ativo, form_type, estimated_days)
SELECT c.id, 'Tapa-Buraco e Pavimentação',
       'Reparos em vias asfálticas ou calçamentos danificados.',
       'Utilize para solicitar o conserto de buracos em vias que estejam atrapalhando o tráfego.',
       'NÃO utilize para solicitar calçamento novo em ruas não construídas ou terrenos particulares.',
       true, true, 'form_pothole_repair', 7
FROM categoria c
WHERE c.nome = 'Zeladoria & Obras'
  AND NOT EXISTS (SELECT 1 FROM servico s WHERE s.nome = 'Tapa-Buraco e Pavimentação');

INSERT INTO servico (categoria_id, nome, descricao, quando_usar, nao_usar_para, requer_endereco, ativo, form_type, estimated_days)
SELECT c.id, 'Recolhimento de Entulho e Metralha',
       'Agende a coleta de restos de obras ensacados na calçada.',
       'Utilize para agendar o recolhimento de resíduos de pequenas reformas depositados em sacos.',
       'NÃO descarte restos de poda e metralha diretamente no leito da rua sem ensacar.',
       true, true, 'form_debris_removal', 5
FROM categoria c
WHERE c.nome = 'Zeladoria & Obras'
  AND NOT EXISTS (SELECT 1 FROM servico s WHERE s.nome = 'Recolhimento de Entulho e Metralha');

INSERT INTO servico (categoria_id, nome, descricao, quando_usar, nao_usar_para, requer_endereco, ativo, form_type, estimated_days)
SELECT c.id, 'Poda de Árvores em Vias',
       'Avaliação de risco de queda ou galhos cobrindo sinalização.',
       'Solicite quando os galhos em praças ou calçadas públicas estiverem com risco de queda.',
       'NÃO solicite a poda de árvores localizadas no quintal ou propriedades privadas.',
       true, true, 'form_tree_pruning', 10
FROM categoria c
WHERE c.nome = 'Zeladoria & Obras'
  AND NOT EXISTS (SELECT 1 FROM servico s WHERE s.nome = 'Poda de Árvores em Vias');

INSERT INTO servico (categoria_id, nome, descricao, quando_usar, nao_usar_para, requer_endereco, ativo, form_type, estimated_days)
SELECT c.id, 'Desobstrução de Galerias/Esgoto',
       'Limpeza de bueiros e redes de drenagem entupidas.',
       'Relate bueiros entupidos, vazamento de esgoto a céu aberto na via pública.',
       'NÃO solicitar para entupimentos dentro de propriedades privadas.',
       true, true, 'form_sewer_repair', 5
FROM categoria c
WHERE c.nome = 'Zeladoria & Obras'
  AND NOT EXISTS (SELECT 1 FROM servico s WHERE s.nome = 'Desobstrução de Galerias/Esgoto');

-- === Saúde Pública ===
INSERT INTO servico (categoria_id, nome, descricao, quando_usar, nao_usar_para, requer_endereco, ativo, form_type, estimated_days)
SELECT c.id, 'Emissão e Atualização do Cartão SUS',
       'Atualize seus dados no sistema nacional de saúde.',
       'Anexe RG, CPF e comprovante de residência atualizado em Carpina.',
       'NÃO envie fotos ilegíveis ou documentos de terceiros sem procuração.',
       false, true, 'form_sus_card', 2
FROM categoria c
WHERE c.nome = 'Saúde Pública'
  AND NOT EXISTS (SELECT 1 FROM servico s WHERE s.nome = 'Emissão e Atualização do Cartão SUS');

INSERT INTO servico (categoria_id, nome, descricao, quando_usar, nao_usar_para, requer_endereco, ativo, form_type, estimated_days)
SELECT c.id, 'Remédio em Casa (Uso Contínuo)',
       'Entrega de medicamentos da farmácia básica para idosos.',
       'Cadastre idosos ou pessoas com mobilidade reduzida para receber medicamentos.',
       'NÃO solicite medicamentos de receita retida (amarela/azul).',
       true, true, 'form_home_medicine', 5
FROM categoria c
WHERE c.nome = 'Saúde Pública'
  AND NOT EXISTS (SELECT 1 FROM servico s WHERE s.nome = 'Remédio em Casa (Uso Contínuo)');

INSERT INTO servico (categoria_id, nome, descricao, quando_usar, nao_usar_para, requer_endereco, ativo, form_type, estimated_days)
SELECT c.id, 'Agendamento UBS',
       'Marque consultas e acompanhamentos preventivos.',
       'Solicite marcação de consultas de rotina no seu posto de saúde do bairro.',
       'NÃO utilize para emergências. Dirija-se à UPA ou ligue 192 (SAMU).',
       false, true, 'form_ubs_scheduling', 3
FROM categoria c
WHERE c.nome = 'Saúde Pública'
  AND NOT EXISTS (SELECT 1 FROM servico s WHERE s.nome = 'Agendamento UBS');

INSERT INTO servico (categoria_id, nome, descricao, quando_usar, nao_usar_para, requer_endereco, ativo, form_type, estimated_days)
SELECT c.id, 'Denúncia - Vigilância Sanitária',
       'Relate falta de higiene em comércios ou terrenos com focos da Dengue.',
       'Denuncie venda de alimentos estragados, água parada ou pragas urbanas.',
       'O anonimato é garantido, mas evite trotes ou falsas acusações.',
       true, true, 'form_sanitary_report', 2
FROM categoria c
WHERE c.nome = 'Saúde Pública'
  AND NOT EXISTS (SELECT 1 FROM servico s WHERE s.nome = 'Denúncia - Vigilância Sanitária');

-- === Educação ===
INSERT INTO servico (categoria_id, nome, descricao, quando_usar, nao_usar_para, requer_endereco, ativo, form_type, estimated_days)
SELECT c.id, 'Fila de Espera - Creches',
       'Cadastre crianças de 0 a 3 anos para vagas em creches.',
       'Acompanhe a posição na fila ou insira nova criança no cadastro.',
       'NÃO falsifique comprovantes de renda. Há análise documental.',
       true, true, 'form_daycare_queue', 15
FROM categoria c
WHERE c.nome = 'Educação'
  AND NOT EXISTS (SELECT 1 FROM servico s WHERE s.nome = 'Fila de Espera - Creches');

INSERT INTO servico (categoria_id, nome, descricao, quando_usar, nao_usar_para, requer_endereco, ativo, form_type, estimated_days)
SELECT c.id, 'Boletim Online',
       'Acesse notas, frequências e ocorrências do aluno.',
       'Necessário número de matrícula do aluno na rede municipal.',
       'Apenas para escolas geridas pela prefeitura de Carpina.',
       false, true, 'form_school_grades', 0
FROM categoria c
WHERE c.nome = 'Educação'
  AND NOT EXISTS (SELECT 1 FROM servico s WHERE s.nome = 'Boletim Online');

INSERT INTO servico (categoria_id, nome, descricao, quando_usar, nao_usar_para, requer_endereco, ativo, form_type, estimated_days)
SELECT c.id, 'Transporte Escolar',
       'Cadastro para rotas de ônibus escolar municipal.',
       'Para alunos da zona rural ou distantes mais de 2km da escola.',
       'Vagas limitadas conforme capacidade dos veículos.',
       true, true, 'form_school_bus', 7
FROM categoria c
WHERE c.nome = 'Educação'
  AND NOT EXISTS (SELECT 1 FROM servico s WHERE s.nome = 'Transporte Escolar');

-- === Fazenda & Tributos ===
INSERT INTO servico (categoria_id, nome, descricao, quando_usar, nao_usar_para, requer_endereco, ativo, form_type, estimated_days)
SELECT c.id, 'Emissão da 2ª Via do IPTU',
       'Gere o boleto atualizado do seu imposto predial.',
       'Informe o código Sequencial para gerar o boleto com desconto.',
       'NÃO efetue pagamentos em PIX que não pertençam à Prefeitura.',
       false, true, 'form_iptu_issue', 1
FROM categoria c
WHERE c.nome = 'Fazenda & Tributos'
  AND NOT EXISTS (SELECT 1 FROM servico s WHERE s.nome = 'Emissão da 2ª Via do IPTU');

INSERT INTO servico (categoria_id, nome, descricao, quando_usar, nao_usar_para, requer_endereco, ativo, form_type, estimated_days)
SELECT c.id, 'Certidão Negativa (CND)',
       'Comprove a regularidade fiscal de PF ou PJ.',
       'Emita a certidão de regularidade perante o município.',
       'NÃO tente emitir se possuir pendências. Solicite parcelamento antes.',
       false, true, 'form_cnd_certificate', 1
FROM categoria c
WHERE c.nome = 'Fazenda & Tributos'
  AND NOT EXISTS (SELECT 1 FROM servico s WHERE s.nome = 'Certidão Negativa (CND)');

INSERT INTO servico (categoria_id, nome, descricao, quando_usar, nao_usar_para, requer_endereco, ativo, form_type, estimated_days)
SELECT c.id, 'Sala do Empreendedor (MEI)',
       'Abertura, regularização e emissão de notas para MEIs.',
       'Agende atendimento para emissão de nota fiscal ou DASN.',
       'NÃO compartilhe suas senhas do portal Gov.br.',
       false, true, 'form_mei_attendance', 3
FROM categoria c
WHERE c.nome = 'Fazenda & Tributos'
  AND NOT EXISTS (SELECT 1 FROM servico s WHERE s.nome = 'Sala do Empreendedor (MEI)');

-- === Social & Cuidado Animal ===
INSERT INTO servico (categoria_id, nome, descricao, quando_usar, nao_usar_para, requer_endereco, ativo, form_type, estimated_days)
SELECT c.id, 'Agendamento CRAS',
       'Atendimento psicológico e assistência familiar.',
       'Marque horário na unidade do CRAS mais próxima do seu bairro.',
       'Chegue com 15 minutos de antecedência no dia agendado.',
       false, true, 'form_cras_schedule', 2
FROM categoria c
WHERE c.nome = 'Social & Cuidado Animal'
  AND NOT EXISTS (SELECT 1 FROM servico s WHERE s.nome = 'Agendamento CRAS');

INSERT INTO servico (categoria_id, nome, descricao, quando_usar, nao_usar_para, requer_endereco, ativo, form_type, estimated_days)
SELECT c.id, 'Consulta CadÚnico',
       'Verifique se seus dados do Governo Federal estão atualizados.',
       'Emita o comprovante de inscrição no Cadastro Único.',
       'Atualizações cadastrais devem ser presenciais.',
       false, true, 'form_cadunico_query', 0
FROM categoria c
WHERE c.nome = 'Social & Cuidado Animal'
  AND NOT EXISTS (SELECT 1 FROM servico s WHERE s.nome = 'Consulta CadÚnico');

INSERT INTO servico (categoria_id, nome, descricao, quando_usar, nao_usar_para, requer_endereco, ativo, form_type, estimated_days)
SELECT c.id, 'Resgate de Animais de Grande Porte',
       'Cavalos ou vacas soltos em vias públicas.',
       'Informe a presença de animais de grande porte soltos nas vias.',
       'NÃO tente conter o animal por conta própria.',
       true, true, 'form_animal_rescue', 1
FROM categoria c
WHERE c.nome = 'Social & Cuidado Animal'
  AND NOT EXISTS (SELECT 1 FROM servico s WHERE s.nome = 'Resgate de Animais de Grande Porte');

INSERT INTO servico (categoria_id, nome, descricao, quando_usar, nao_usar_para, requer_endereco, ativo, form_type, estimated_days)
SELECT c.id, 'Agendamento de Castração',
       'Castração gratuita para pets de famílias de baixa renda.',
       'Cadastre animais resgatados ou com tutor inscrito no CadÚnico.',
       'Faltas injustificadas bloqueiam novos agendamentos por 6 meses.',
       false, true, 'form_castration_schedule', 15
FROM categoria c
WHERE c.nome = 'Social & Cuidado Animal'
  AND NOT EXISTS (SELECT 1 FROM servico s WHERE s.nome = 'Agendamento de Castração');

-- === Mobilidade & Trânsito ===
INSERT INTO servico (categoria_id, nome, descricao, quando_usar, nao_usar_para, requer_endereco, ativo, form_type, estimated_days)
SELECT c.id, 'Carteira do Transporte Universitário',
       'Passe livre para estudantes universitários de Carpina.',
       'Envie comprovante de matrícula para emissão do passe livre.',
       'NÃO envie declarações expiradas.',
       true, true, 'form_university_pass', 10
FROM categoria c
WHERE c.nome = 'Mobilidade & Trânsito'
  AND NOT EXISTS (SELECT 1 FROM servico s WHERE s.nome = 'Carteira do Transporte Universitário');

INSERT INTO servico (categoria_id, nome, descricao, quando_usar, nao_usar_para, requer_endereco, ativo, form_type, estimated_days)
SELECT c.id, 'Credencial Estacionamento (Idoso/PCD)',
       'Autorização para uso de vagas reservadas no município.',
       'Solicite o cartão obrigatório para vagas especiais.',
       'A credencial é pessoal e intransferível.',
       true, true, 'form_parking_credential', 5
FROM categoria c
WHERE c.nome = 'Mobilidade & Trânsito'
  AND NOT EXISTS (SELECT 1 FROM servico s WHERE s.nome = 'Credencial Estacionamento (Idoso/PCD)');

INSERT INTO servico (categoria_id, nome, descricao, quando_usar, nao_usar_para, requer_endereco, ativo, form_type, estimated_days)
SELECT c.id, 'Manutenção de Semáforo / Placas',
       'Relate semáforos quebrados ou placas caídas.',
       'Notifique o órgão de trânsito sobre perigos imediatos na via.',
       'NÃO instale quebra-molas por conta própria (infração grave).',
       true, true, 'form_traffic_signaling', 2
FROM categoria c
WHERE c.nome = 'Mobilidade & Trânsito'
  AND NOT EXISTS (SELECT 1 FROM servico s WHERE s.nome = 'Manutenção de Semáforo / Placas');

INSERT INTO servico (categoria_id, nome, descricao, quando_usar, nao_usar_para, requer_endereco, ativo, form_type, estimated_days)
SELECT c.id, 'Recurso de Multa (Defesa Prévia)',
       'Conteste infrações aplicadas por agentes municipais.',
       'Anexe provas e a notificação de autuação dentro do prazo legal.',
       'Apenas para multas emitidas pela autarquia de trânsito municipal.',
       false, true, 'form_traffic_fine_appeal', 30
FROM categoria c
WHERE c.nome = 'Mobilidade & Trânsito'
  AND NOT EXISTS (SELECT 1 FROM servico s WHERE s.nome = 'Recurso de Multa (Defesa Prévia)');

-- ----------------------------------------------------------------------------
-- 3. DOCUMENTOS NECESSÁRIOS POR SERVIÇO (servico_documentos)
-- ----------------------------------------------------------------------------
INSERT INTO servico_documentos (servico_id, documento)
SELECT s.id, 'Foto do Poste'
FROM servico s
WHERE s.nome = 'Reparo de Iluminação Pública'
  AND NOT EXISTS (SELECT 1 FROM servico_documentos d WHERE d.servico_id = s.id AND d.documento = 'Foto do Poste');

INSERT INTO servico_documentos (servico_id, documento)
SELECT s.id, 'Foto do Local'
FROM servico s
WHERE s.nome = 'Tapa-Buraco e Pavimentação'
  AND NOT EXISTS (SELECT 1 FROM servico_documentos d WHERE d.servico_id = s.id AND d.documento = 'Foto do Local');

INSERT INTO servico_documentos (servico_id, documento)
SELECT s.id, 'Foto da Árvore'
FROM servico s
WHERE s.nome = 'Poda de Árvores em Vias'
  AND NOT EXISTS (SELECT 1 FROM servico_documentos d WHERE d.servico_id = s.id AND d.documento = 'Foto da Árvore');

INSERT INTO servico_documentos (servico_id, documento)
SELECT s.id, 'Foto do Local'
FROM servico s
WHERE s.nome = 'Desobstrução de Galerias/Esgoto'
  AND NOT EXISTS (SELECT 1 FROM servico_documentos d WHERE d.servico_id = s.id AND d.documento = 'Foto do Local');

INSERT INTO servico_documentos (servico_id, documento)
SELECT s.id, 'RG/CPF'
FROM servico s
WHERE s.nome = 'Emissão e Atualização do Cartão SUS'
  AND NOT EXISTS (SELECT 1 FROM servico_documentos d WHERE d.servico_id = s.id AND d.documento = 'RG/CPF');

INSERT INTO servico_documentos (servico_id, documento)
SELECT s.id, 'Comprovante de Residência recente'
FROM servico s
WHERE s.nome = 'Emissão e Atualização do Cartão SUS'
  AND NOT EXISTS (SELECT 1 FROM servico_documentos d WHERE d.servico_id = s.id AND d.documento = 'Comprovante de Residência recente');

INSERT INTO servico_documentos (servico_id, documento)
SELECT s.id, 'Receita Médica Atualizada'
FROM servico s
WHERE s.nome = 'Remédio em Casa (Uso Contínuo)'
  AND NOT EXISTS (SELECT 1 FROM servico_documentos d WHERE d.servico_id = s.id AND d.documento = 'Receita Médica Atualizada');

INSERT INTO servico_documentos (servico_id, documento)
SELECT s.id, 'Documento do Paciente'
FROM servico s
WHERE s.nome = 'Remédio em Casa (Uso Contínuo)'
  AND NOT EXISTS (SELECT 1 FROM servico_documentos d WHERE d.servico_id = s.id AND d.documento = 'Documento do Paciente');

INSERT INTO servico_documentos (servico_id, documento)
SELECT s.id, 'Cartão SUS'
FROM servico s
WHERE s.nome = 'Agendamento UBS'
  AND NOT EXISTS (SELECT 1 FROM servico_documentos d WHERE d.servico_id = s.id AND d.documento = 'Cartão SUS');

INSERT INTO servico_documentos (servico_id, documento)
SELECT s.id, 'Foto/Vídeo (Opcional)'
FROM servico s
WHERE s.nome = 'Denúncia - Vigilância Sanitária'
  AND NOT EXISTS (SELECT 1 FROM servico_documentos d WHERE d.servico_id = s.id AND d.documento = 'Foto/Vídeo (Opcional)');

INSERT INTO servico_documentos (servico_id, documento)
SELECT s.id, 'Certidão de Nascimento'
FROM servico s
WHERE s.nome = 'Fila de Espera - Creches'
  AND NOT EXISTS (SELECT 1 FROM servico_documentos d WHERE d.servico_id = s.id AND d.documento = 'Certidão de Nascimento');

INSERT INTO servico_documentos (servico_id, documento)
SELECT s.id, 'Comprovante de Residência'
FROM servico s
WHERE s.nome = 'Fila de Espera - Creches'
  AND NOT EXISTS (SELECT 1 FROM servico_documentos d WHERE d.servico_id = s.id AND d.documento = 'Comprovante de Residência');

INSERT INTO servico_documentos (servico_id, documento)
SELECT s.id, 'Matrícula do Aluno'
FROM servico s
WHERE s.nome = 'Boletim Online'
  AND NOT EXISTS (SELECT 1 FROM servico_documentos d WHERE d.servico_id = s.id AND d.documento = 'Matrícula do Aluno');

INSERT INTO servico_documentos (servico_id, documento)
SELECT s.id, 'Declaração Escolar'
FROM servico s
WHERE s.nome = 'Transporte Escolar'
  AND NOT EXISTS (SELECT 1 FROM servico_documentos d WHERE d.servico_id = s.id AND d.documento = 'Declaração Escolar');

INSERT INTO servico_documentos (servico_id, documento)
SELECT s.id, 'Número de Inscrição do Imóvel'
FROM servico s
WHERE s.nome = 'Emissão da 2ª Via do IPTU'
  AND NOT EXISTS (SELECT 1 FROM servico_documentos d WHERE d.servico_id = s.id AND d.documento = 'Número de Inscrição do Imóvel');

INSERT INTO servico_documentos (servico_id, documento)
SELECT s.id, 'CPF/CNPJ'
FROM servico s
WHERE s.nome = 'Certidão Negativa (CND)'
  AND NOT EXISTS (SELECT 1 FROM servico_documentos d WHERE d.servico_id = s.id AND d.documento = 'CPF/CNPJ');

INSERT INTO servico_documentos (servico_id, documento)
SELECT s.id, 'CNPJ MEI'
FROM servico s
WHERE s.nome = 'Sala do Empreendedor (MEI)'
  AND NOT EXISTS (SELECT 1 FROM servico_documentos d WHERE d.servico_id = s.id AND d.documento = 'CNPJ MEI');

INSERT INTO servico_documentos (servico_id, documento)
SELECT s.id, 'Acesso Gov.br'
FROM servico s
WHERE s.nome = 'Sala do Empreendedor (MEI)'
  AND NOT EXISTS (SELECT 1 FROM servico_documentos d WHERE d.servico_id = s.id AND d.documento = 'Acesso Gov.br');

INSERT INTO servico_documentos (servico_id, documento)
SELECT s.id, 'NIS'
FROM servico s
WHERE s.nome = 'Agendamento CRAS'
  AND NOT EXISTS (SELECT 1 FROM servico_documentos d WHERE d.servico_id = s.id AND d.documento = 'NIS');

INSERT INTO servico_documentos (servico_id, documento)
SELECT s.id, 'RG/CPF'
FROM servico s
WHERE s.nome = 'Agendamento CRAS'
  AND NOT EXISTS (SELECT 1 FROM servico_documentos d WHERE d.servico_id = s.id AND d.documento = 'RG/CPF');

INSERT INTO servico_documentos (servico_id, documento)
SELECT s.id, 'CPF'
FROM servico s
WHERE s.nome = 'Consulta CadÚnico'
  AND NOT EXISTS (SELECT 1 FROM servico_documentos d WHERE d.servico_id = s.id AND d.documento = 'CPF');

INSERT INTO servico_documentos (servico_id, documento)
SELECT s.id, 'Ponto de Referência / Foto'
FROM servico s
WHERE s.nome = 'Resgate de Animais de Grande Porte'
  AND NOT EXISTS (SELECT 1 FROM servico_documentos d WHERE d.servico_id = s.id AND d.documento = 'Ponto de Referência / Foto');

INSERT INTO servico_documentos (servico_id, documento)
SELECT s.id, 'Comprovante do CadÚnico'
FROM servico s
WHERE s.nome = 'Agendamento de Castração'
  AND NOT EXISTS (SELECT 1 FROM servico_documentos d WHERE d.servico_id = s.id AND d.documento = 'Comprovante do CadÚnico');

INSERT INTO servico_documentos (servico_id, documento)
SELECT s.id, 'RG do Tutor'
FROM servico s
WHERE s.nome = 'Agendamento de Castração'
  AND NOT EXISTS (SELECT 1 FROM servico_documentos d WHERE d.servico_id = s.id AND d.documento = 'RG do Tutor');

INSERT INTO servico_documentos (servico_id, documento)
SELECT s.id, 'Comprovante de Matrícula'
FROM servico s
WHERE s.nome = 'Carteira do Transporte Universitário'
  AND NOT EXISTS (SELECT 1 FROM servico_documentos d WHERE d.servico_id = s.id AND d.documento = 'Comprovante de Matrícula');

INSERT INTO servico_documentos (servico_id, documento)
SELECT s.id, 'Comprovante de Residência'
FROM servico s
WHERE s.nome = 'Carteira do Transporte Universitário'
  AND NOT EXISTS (SELECT 1 FROM servico_documentos d WHERE d.servico_id = s.id AND d.documento = 'Comprovante de Residência');

INSERT INTO servico_documentos (servico_id, documento)
SELECT s.id, 'Laudo Médico (PCD)'
FROM servico s
WHERE s.nome = 'Credencial Estacionamento (Idoso/PCD)'
  AND NOT EXISTS (SELECT 1 FROM servico_documentos d WHERE d.servico_id = s.id AND d.documento = 'Laudo Médico (PCD)');

INSERT INTO servico_documentos (servico_id, documento)
SELECT s.id, 'RG/CPF'
FROM servico s
WHERE s.nome = 'Credencial Estacionamento (Idoso/PCD)'
  AND NOT EXISTS (SELECT 1 FROM servico_documentos d WHERE d.servico_id = s.id AND d.documento = 'RG/CPF');

INSERT INTO servico_documentos (servico_id, documento)
SELECT s.id, 'Foto do Local'
FROM servico s
WHERE s.nome = 'Manutenção de Semáforo / Placas'
  AND NOT EXISTS (SELECT 1 FROM servico_documentos d WHERE d.servico_id = s.id AND d.documento = 'Foto do Local');

INSERT INTO servico_documentos (servico_id, documento)
SELECT s.id, 'Notificação da Multa'
FROM servico s
WHERE s.nome = 'Recurso de Multa (Defesa Prévia)'
  AND NOT EXISTS (SELECT 1 FROM servico_documentos d WHERE d.servico_id = s.id AND d.documento = 'Notificação da Multa');

INSERT INTO servico_documentos (servico_id, documento)
SELECT s.id, 'CNH'
FROM servico s
WHERE s.nome = 'Recurso de Multa (Defesa Prévia)'
  AND NOT EXISTS (SELECT 1 FROM servico_documentos d WHERE d.servico_id = s.id AND d.documento = 'CNH');

INSERT INTO servico_documentos (servico_id, documento)
SELECT s.id, 'CRLV'
FROM servico s
WHERE s.nome = 'Recurso de Multa (Defesa Prévia)'
  AND NOT EXISTS (SELECT 1 FROM servico_documentos d WHERE d.servico_id = s.id AND d.documento = 'CRLV');

-- ----------------------------------------------------------------------------
-- 4. PONTOS TURÍSTICOS (evento_turismo)
-- Pontos são permanentes -> datas amplas pra sempre aparecerem na listagem
-- ----------------------------------------------------------------------------
INSERT INTO evento_turismo (titulo, descricao, categoria, data_inicio, data_fim, local, imagem_url, rating)
SELECT 'Parque Anagé Góis',
       'Área verde com pista de caminhada, parque infantil e lago para passeios em família.',
       'Ecoturismo e Lazer',
       '2026-01-01 00:00:00', '2030-12-31 23:59:59',
       'Bairro Novo, Carpina - PE',
       'https://www.carpina.pe.gov.br/wp-content/uploads/2026/08/8ae70524-870d-4cc2-9da3-d238e3308098-700x400.jpeg',
       4.8
WHERE NOT EXISTS (SELECT 1 FROM evento_turismo e WHERE e.titulo = 'Parque Anagé Góis');

INSERT INTO evento_turismo (titulo, descricao, categoria, data_inicio, data_fim, local, imagem_url, rating)
SELECT 'Praça São José',
       'Coração da cidade de Carpina, palco dos principais eventos culturais, feiras e gastronomia local.',
       'Centro Histórico',
       '2026-01-01 00:00:00', '2030-12-31 23:59:59',
       'Centro, Carpina - PE',
       'https://www.carpina.pe.gov.br/wp-content/uploads/2026/06/6d663041-8c14-4553-b7fa-5f137dc2dd7c-800x500.jpeg',
       4.7
WHERE NOT EXISTS (SELECT 1 FROM evento_turismo e WHERE e.titulo = 'Praça São José');

INSERT INTO evento_turismo (titulo, descricao, categoria, data_inicio, data_fim, local, imagem_url, rating)
SELECT 'Igreja Matriz de São José',
       'Construção imponente e marco da fundação do município, com belos vitrais e missas tradicionais.',
       'Religioso e Histórico',
       '2026-01-01 00:00:00', '2030-12-31 23:59:59',
       'Rua do Sol, Centro',
       'https://via.placeholder.com/800x500.png?text=Igreja+Matriz',
       4.9
WHERE NOT EXISTS (SELECT 1 FROM evento_turismo e WHERE e.titulo = 'Igreja Matriz de São José');

INSERT INTO evento_turismo (titulo, descricao, categoria, data_inicio, data_fim, local, imagem_url, rating)
SELECT 'Museu do Mamulengo',
       'Espaço dedicado à preservação da arte dos mamulengos, rica tradição cultural da Mata Norte pernambucana.',
       'Cultura Popular',
       '2026-01-01 00:00:00', '2030-12-31 23:59:59',
       'Av. Joaquim Pinto Lapa',
       'https://via.placeholder.com/800x500.png?text=Museu+Mamulengo',
       4.6
WHERE NOT EXISTS (SELECT 1 FROM evento_turismo e WHERE e.titulo = 'Museu do Mamulengo');