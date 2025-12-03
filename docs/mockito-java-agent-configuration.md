# Configuração do Mockito como Java Agent

## Problema Resolvido

O Mockito estava emitindo o seguinte aviso durante a execução dos testes:

```
Mockito is currently self-attaching to enable the inline-mock-maker. 
This will no longer work in future releases of the JDK. 
Please add Mockito as an agent to your build as described in Mockito's documentation.
```

## Solução Implementada

Para resolver este problema e garantir compatibilidade com versões futuras do JDK (especialmente Java 23 e posteriores), o Mockito foi configurado como um Java agent no Maven.

## Alterações no pom.xml

### 1. Adição de Propriedade para Versão do Mockito

```xml
<properties>
    ...
    <mockito.version>5.17.0</mockito.version>
</properties>
```

### 2. Adição de Dependência Explícita do Mockito Core

```xml
<!-- Mockito Core (necessário como Java agent) -->
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <version>${mockito.version}</version>
    <scope>test</scope>
</dependency>
```

### 3. Configuração do Maven Surefire Plugin

```xml
<!-- Plugin de testes com Mockito como agente -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <configuration>
        <argLine>
            -javaagent:${settings.localRepository}/org/mockito/mockito-core/${mockito.version}/mockito-core-${mockito.version}.jar
        </argLine>
    </configuration>
</plugin>
```

## Resultado

✅ O aviso do Mockito sobre self-attaching foi completamente eliminado  
✅ Todos os testes continuam funcionando normalmente  
✅ Compatibilidade garantida com versões futuras do JDK  
✅ Performance dos testes mantida ou melhorada  

## Validação

Execute os testes para confirmar que o aviso não aparece mais:

```bash
./mvnw test
```

## Referências

- [Documentação oficial do Mockito sobre Java Agent](https://javadoc.io/doc/org.mockito/mockito-core/latest/org.mockito/org/mockito/Mockito.html#0.3)
- [Mockito Inline Mock Maker](https://github.com/mockito/mockito/wiki/What%27s-new-in-Mockito-2#unmockable)

## Data de Implementação

02 de dezembro de 2025

