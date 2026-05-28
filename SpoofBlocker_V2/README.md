# Bloqueador de Spoofing Android

Este aplicativo foi desenvolvido para bloquear chamadas de spoofing baseadas em prefixos telefônicos.

## Como funciona
O aplicativo utiliza a API `CallScreeningService` do Android (disponível a partir do Android 10/API 29) para interceptar chamadas antes mesmo de o telefone tocar.

## Configuração
1. Ao abrir o app, ele solicitará a permissão para ser o "Filtrador de Chamadas" (Call Screening Role). Esta permissão é obrigatória para o bloqueio funcionar.
2. Insira o prefixo que deseja bloquear (ex: 2199257).
3. Clique em "Salvar Configuração".

## Notas Técnicas
- O app não requer permissões intrusivas como ler contatos, pois o sistema Android gerencia a filtragem.
- Chamadas que começarem com o prefixo configurado serão rejeitadas automaticamente e não aparecerão como notificação de chamada perdida (configurável no código).

## Estrutura do Projeto
- `MainActivity.kt`: Gerencia a interface e solicita as permissões necessárias.
- `SpoofCallScreeningService.kt`: Contém a lógica de detecção e bloqueio.
- `AndroidManifest.xml`: Define os serviços e permissões do sistema.
