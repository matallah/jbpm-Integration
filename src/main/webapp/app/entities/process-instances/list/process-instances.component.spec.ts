import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ProcessInstancesService } from '../service/process-instances.service';

import { ProcessInstancesComponent } from './process-instances.component';

describe('ProcessInstances Management Component', () => {
  let comp: ProcessInstancesComponent;
  let fixture: ComponentFixture<ProcessInstancesComponent>;
  let service: ProcessInstancesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'process-instances', component: ProcessInstancesComponent }]),
        HttpClientTestingModule,
        ProcessInstancesComponent,
      ],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(ProcessInstancesComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProcessInstancesComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ProcessInstancesService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.processInstances?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to processInstancesService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getProcessInstancesIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getProcessInstancesIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
